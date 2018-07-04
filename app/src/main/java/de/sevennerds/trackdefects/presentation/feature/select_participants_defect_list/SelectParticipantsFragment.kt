package de.sevennerds.trackdefects.presentation.feature.select_participants_defect_list

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.view.clicks
import com.wafflecopter.multicontactpicker.ContactResult
import com.wafflecopter.multicontactpicker.MultiContactPicker
import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.TrackDefectsApp
import de.sevennerds.trackdefects.common.hideKeyboard
import de.sevennerds.trackdefects.common.toObservable
import de.sevennerds.trackdefects.presentation.MainActivity
import de.sevennerds.trackdefects.presentation.base.BaseFragment
import de.sevennerds.trackdefects.presentation.feature.select_participants_defect_list.list.SelectParticipantsListAdapter
import de.sevennerds.trackdefects.presentation.feature.take_ground_plan_picture.navigation.TakeGroundPlanPictureKey
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_select_participants.*
import javax.inject.Inject


/**
 * Takes care of saving the parcelState
 * Accesses the ViewModel via compose(eventTransformer)
 * Renders the parcelState
 */

class SelectParticipantsFragment : BaseFragment() {

    @Inject
    lateinit var viewModel: SelectParticipantsViewModel

    private val initEventSubject: PublishSubject<SelectParticipantsView.Event.Init> =
            PublishSubject.create()

    private val addEventBSubject: BehaviorSubject<SelectParticipantsView.Event.Add> =
            BehaviorSubject.create()

    private val compositeDisposable = CompositeDisposable()

    companion object {
        private const val CONTACT_PICKER_REQUEST = 991
    }

    private var parcelState: SelectParticipantsView.ParcelState? = null

    private lateinit var listAdapter: SelectParticipantsListAdapter

    private var isRotation = false

    private val contactBuilder =
            MultiContactPicker.Builder(this)
                    .hideScrollbar(false)
                    .showTrack(true)
                    .searchIconColor(Color.WHITE)
                    .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE)
                    .bubbleTextColor(Color.WHITE)


    override fun onAttach(context: Context?) {

        TrackDefectsApp.get(context!!)
                .appComponent
                .inject(this)

        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parcelState = savedInstanceState?.getParcelable(KEY_STATE_PARCEL)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        isRotation = false

        return inflater.inflate(R.layout.fragment_select_participants,
                                container,
                                false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setup()
        init()
    }

    override fun onPause() {
        super.onPause()

        isRotation = true

        compositeDisposable.clear()
    }


    override fun onSaveInstanceState(outState: Bundle) {

        parcelState?.let {
            outState.putParcelable(KEY_STATE_PARCEL, it)
        }

        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()

        if (isRotation) {
            setupEvents()
            isRotation = false
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CONTACT_PICKER_REQUEST) {
            when (resultCode) {
                RESULT_OK ->
                    contactResult(MultiContactPicker.obtainResult(data))
            }
        }

    }

// ------------------------------------------------------------------------------------------

    private fun setup() {
        setupActionBar()
        setupRecyclerView()
        setupEvents()
    }

    private fun setupActionBar() {
        MainActivity[requireContext()].supportActionBar?.title = "Teilnehmer"
    }

    private fun setupRecyclerView() {

        with(select_participants_rcv) {
            layoutManager = LinearLayoutManager(context)
            listAdapter = SelectParticipantsListAdapter(mutableListOf())
            adapter = listAdapter
        }
    }

    private fun setupEvents() {

        compositeDisposable += Observable
                .mergeArray(
                        select_participants_fab
                                .clicks()
                                .map { SelectParticipantsView.Event.ShowContacts },
                        listAdapter
                                .getOnItemClickListener()
                                .map { itemPosition ->
                                    SelectParticipantsView.Event.Remove(itemPosition,
                                                                        listAdapter.getList())
                                },
                        select_participants_next_skip_btn
                                .clicks()
                                .map { SelectParticipantsView.Event.Next },

                        initEventSubject.toObservable(),
                        addEventBSubject.toObservable())
                .compose(viewModel.eventToViewState)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::render)
    }

    private fun init() {

        initEventSubject.onNext(
                SelectParticipantsView.Event
                        .Init(parcelState
                                      ?: SelectParticipantsView.ParcelState(emptyList())))
    }

    private fun contactResult(contactResultList: List<ContactResult>) {

        addEventBSubject.onNext(SelectParticipantsView.Event
                                        .Add(contactResultList,
                                             listAdapter.getList()))
    }

    private fun render(viewState: SelectParticipantsView.State) {

        val renderState = viewState.renderState

        return when (renderState) {

            is SelectParticipantsView.RenderState.ShowContacts ->
                contactBuilder
                        .showPickerForResult(CONTACT_PICKER_REQUEST)

            is SelectParticipantsView.RenderState.Init -> {
                updateParcelState(renderState.parcelState)
                updateList(viewState.participantModelList)
                updateButtonText(viewState.nextOrSkipButtonText)
            }

            is SelectParticipantsView.RenderState.Add -> {
                updateParcelState(renderState.parcelState)
                updateList(viewState.participantModelList,
                           renderState.diffResult)
                updateButtonText(viewState.nextOrSkipButtonText)
            }

            is SelectParticipantsView.RenderState.Remove -> {
                updateParcelState(renderState.parcelState)
                updateList(viewState.participantModelList,
                           renderState.diffResult)
                updateButtonText(viewState.nextOrSkipButtonText)
            }

            is SelectParticipantsView.RenderState.Next -> {

                hideKeyboard()

                MainActivity[requireContext()].navigateTo(TakeGroundPlanPictureKey())
            }

            is SelectParticipantsView.RenderState.None -> {
            }
        }
    }

    // TODO: is suppose to change the buttons text from "Skip" to "Next"
    private fun updateButtonText(text: String) {
        select_participants_next_skip_btn.text = text
    }

    private fun updateList(newParticipantModeList: List<ParticipantModel>) =
            updateList(newParticipantModeList, null)

    private fun updateList(newParticipantModeList: List<ParticipantModel>,
                           diffResult: DiffUtil.DiffResult?) =

            with(listAdapter) {
                clearList()
                addAllToList(newParticipantModeList)
                diffResult?.dispatchUpdatesTo(this) ?: notifyDataSetChanged()
            }

    private fun updateParcelState(parcelState: SelectParticipantsView.ParcelState) {
        this.parcelState = parcelState
    }
}
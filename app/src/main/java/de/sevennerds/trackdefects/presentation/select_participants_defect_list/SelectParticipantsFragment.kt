package de.sevennerds.trackdefects.presentation.select_participants_defect_list

import android.app.Activity.RESULT_OK
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
import de.sevennerds.trackdefects.common.asObservable
import de.sevennerds.trackdefects.presentation.MainActivity
import de.sevennerds.trackdefects.presentation.base.BaseFragment
import de.sevennerds.trackdefects.presentation.select_participants_defect_list.list.SelectParticipantsListAdapter
import de.sevennerds.trackdefects.presentation.take_ground_plan_picture.navigation.TakeGroundPlanPictureKey
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_select_participants.*


/**
 * Takes care of saving the state
 * Accesses the ViewModel via compose(eventTransformer)
 * Renders the state
 */
class SelectParticipantsFragment : BaseFragment() {

    private val KEY_STATE = "KEY_STATE"
    private val CONTACT_PICKER_REQUEST = 991

    private val compositeDisposable = CompositeDisposable()

    private lateinit var listAdapter: SelectParticipantsListAdapter
    private lateinit var viewModel: SelectParticipantsViewModel

    private var isRotation = false

    private val contactBuilder =
            MultiContactPicker.Builder(this)
                    .hideScrollbar(false)
                    .showTrack(true)
                    .searchIconColor(Color.WHITE)
                    .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE)
                    .bubbleTextColor(Color.WHITE)


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

        viewModel = SelectParticipantsViewModel(
                SelectParticipantsView.State(
                        savedInstanceState
                                ?.getParcelable<SelectParticipantsView.StateParcel>(KEY_STATE)
                                ?.participantModelList
                                ?: emptyList(),
                        null))

        setup()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(KEY_STATE,
                               viewModel.getViewStateParcel())
    }

    override fun onPause() {
        super.onPause()

        isRotation = true

        compositeDisposable.clear()
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
            /* RESULT_CANCELED -> */
        }

    }

// ------------------------------------------------------------------------------------------

    private fun setup() {
        setupActionBar()
        setupRecyclerView()
        setupEvents()
    }

    private fun setupActionBar() {
        MainActivity[context!!].supportActionBar?.title = "Teilnehmer"
    }

    private fun setupRecyclerView() {

        with(select_participants_rcv) {
            layoutManager = LinearLayoutManager(context)
            listAdapter = SelectParticipantsListAdapter(mutableListOf())
            adapter = listAdapter
        }
    }

    private fun setupEvents() {

        compositeDisposable += Observable.fromCallable {
            SelectParticipantsView.Event.Init
        }
                .compose(viewModel.eventTransformer)
                .subscribe(::render)

        compositeDisposable += select_participants_fab
                .clicks()
                .subscribe {

                    contactBuilder
                            .showPickerForResult(CONTACT_PICKER_REQUEST)
                }

        compositeDisposable += listAdapter
                .getOnItemClickListener()
                .map { itemPosition ->
                    SelectParticipantsView.Event
                            .Remove(itemPosition,
                                    listAdapter.getList())
                }
                .compose(viewModel.eventTransformer)
                .subscribe(::render)

        compositeDisposable += select_participants_next_skip_btn
                .clicks()
                .subscribe {
                    MainActivity[context!!].navigateTo(TakeGroundPlanPictureKey())
                }
    }

    private fun contactResult(contactResultList: List<ContactResult>) {

        compositeDisposable +=
                SelectParticipantsView.Event
                        .Add(contactResultList,
                             listAdapter.getList())
                        .asObservable()
                        .compose(viewModel.eventTransformer)
                        .subscribe(::render)
    }

    private fun render(renderState: SelectParticipantsView.RenderState) {

        return when (renderState) {

            is SelectParticipantsView.RenderState.Init ->
                updateList(renderState.participantModelList)

            is SelectParticipantsView.RenderState.Add -> {

                updateList(renderState.participantModelList,
                           renderState.diffResult)
            }

            is SelectParticipantsView.RenderState.Remove -> {
                updateList(renderState.participantModelList,
                           renderState.diffResult)
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
}
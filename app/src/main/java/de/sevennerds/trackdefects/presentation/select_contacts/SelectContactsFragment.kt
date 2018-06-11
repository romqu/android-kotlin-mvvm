package de.sevennerds.trackdefects.presentation.select_contacts

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.view.clicks
import com.orhanobut.logger.Logger
import com.wafflecopter.multicontactpicker.ContactResult
import com.wafflecopter.multicontactpicker.MultiContactPicker
import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.common.asObservable
import de.sevennerds.trackdefects.presentation.MainActivity
import de.sevennerds.trackdefects.presentation.base.BaseFragment
import de.sevennerds.trackdefects.presentation.select_contacts.list.SelectContactsAdapter
import de.sevennerds.trackdefects.presentation.take_ground_plan_image.navigation.TakeGroundPlanImageKey
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_select_contacts.*


class SelectContactsFragment : BaseFragment() {

    private val KEY_STATE = "KEY"
    private var isRotation = false

    private lateinit var viewStateP: ViewStateP

    private lateinit var listAdapter: SelectContactsAdapter

    private val contactBuilder =
            MultiContactPicker.Builder(this)
                    .hideScrollbar(false)
                    .showTrack(true)
                    .searchIconColor(Color.WHITE)
                    .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE)
                    .bubbleTextColor(Color.WHITE)

    private val CONTACT_PICKER_REQUEST = 991
    private val compositeDisposable = CompositeDisposable()

    private lateinit var viewModel: SelectContactsViewModel

    // Fragment override methods

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        isRotation = false

        Logger.d("onCreateView: ${savedInstanceState?.getParcelable<ViewStateP>(KEY_STATE)}")

        return inflater.inflate(R.layout.fragment_select_contacts,
                                container,
                                false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Logger.d("onActivityCreated: ${savedInstanceState?.getParcelable<ViewStateP>(KEY_STATE)}")

        viewModel = SelectContactsViewModel(
                ViewState(savedInstanceState
                                  ?.getParcelable<ViewStateP>(KEY_STATE)?.contactModelList
                                  ?: emptyList(),
                          null))

        setup()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        Logger.d("onViewStateRestored: ${savedInstanceState?.getParcelable<ViewStateP>(KEY_STATE)}")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        Logger.d("onSaveInstanceState: ${viewModel.getViewStateP()}")

        outState.putParcelable(KEY_STATE, viewModel.getViewStateP())
    }

    override fun onStart() {
        super.onStart()

        Logger.d("onStart")
    }


    // save state into variable
    override fun onPause() {
        super.onPause()

        Logger.d("onPause")

        isRotation = true

        viewStateP = viewModel.getViewStateP()

        compositeDisposable.clear()
    }

    override fun onResume() {
        super.onResume()

        if (isRotation) {
            setupReactive()
            isRotation = false
        }

        Logger.d("onResume")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CONTACT_PICKER_REQUEST) {
            when (resultCode) {
                RESULT_OK ->
                    contactResult(MultiContactPicker.obtainResult(data))
            }
            /* RESULT_CANCELED ->
                 contactResult(emptyList())*/
        }

    }

// ------------------------------------------------------------------------------------------

    private fun setup() {
        setupActionBar()
        setupRecyclerView()
/*
         compositeDisposable += Observable.fromCallable { SelectContactsEvent.Init }
                 .compose(viewModel.eventTransformer)
                 .subscribe(::render)*/

        setupReactive()
    }

    private fun setupActionBar() {
        (activity as MainActivity).supportActionBar?.title = "Teilnehmer"

    }

    private fun setupRecyclerView() {

        with(select_contacts_rcv) {
            layoutManager = (LinearLayoutManager(context))
            listAdapter = SelectContactsAdapter(mutableListOf())
            adapter = listAdapter
        }

    }

    private fun setupReactive() {

        compositeDisposable += select_contacts_fab
                .clicks()
                .subscribe {

                    contactBuilder
                            .showPickerForResult(CONTACT_PICKER_REQUEST)
                }

        compositeDisposable += listAdapter
                .onClickSubject
                .map {
                    SelectContactsEvent
                            .RemoveContactEvent(it,
                                                listAdapter.getList())
                }
                .compose(viewModel.eventTransformer)
                .subscribe(::render)

        compositeDisposable += select_contacts_next_skip_btn
                .clicks()
                .subscribe {
                    MainActivity[context!!].navigateTo(TakeGroundPlanImageKey())
                }
    }

    private fun contactResult(contactResultList: List<ContactResult>) {

        compositeDisposable +=
                SelectContactsEvent
                        .SelectContactEvent(contactResultList,
                                            listAdapter.getList())
                        .asObservable()
                        .compose(viewModel.eventTransformer)
                        .subscribe(::render)
    }

    private fun render(viewRenderState: ViewRenderState) {

        return when (viewRenderState) {

            is ViewRenderState.Init -> updateList(viewRenderState.contactModelList)

            is ViewRenderState.SelectContact -> {
                select_contacts_next_skip_btn.text = "Next"

                with(listAdapter) {
                    clearList()
                    /*val list = mutableListOf<ContactModel>()
                    for (i in 0..20) {
                        list.add(viewRenderState.contactModelList.first().copy(name = i.toString()))
                    }*/
                    addAllToList(viewRenderState.contactModelList)
                    viewRenderState.diffResult.dispatchUpdatesTo(this)
                }
            }

            is ViewRenderState.RemoveContact -> {
                with(listAdapter) {
                    clearList()
                    addAllToList(viewRenderState.contactModelList)
                    viewRenderState.diffResult.dispatchUpdatesTo(this)
                }
            }
        }
    }

    private fun updateList(newContactModeList: List<ContactModel>) {
        with(listAdapter) {
            clearList()
            addAllToList(newContactModeList)
            notifyDataSetChanged()
        }
    }
}
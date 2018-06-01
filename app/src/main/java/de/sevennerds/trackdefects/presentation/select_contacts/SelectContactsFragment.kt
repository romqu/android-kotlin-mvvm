package de.sevennerds.trackdefects.presentation.select_contacts

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.jakewharton.rxbinding2.view.clicks
import com.orhanobut.logger.Logger
import com.wafflecopter.multicontactpicker.ContactResult
import com.wafflecopter.multicontactpicker.MultiContactPicker
import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.common.Constants
import de.sevennerds.trackdefects.common.applySchedulers
import de.sevennerds.trackdefects.data.LocalDataSource
import de.sevennerds.trackdefects.data.client.ClientEntity
import de.sevennerds.trackdefects.data.test.TestEntity
import de.sevennerds.trackdefects.data.test.TestLocalData
import de.sevennerds.trackdefects.data.test.TestLocalDb
import de.sevennerds.trackdefects.presentation.MainActivity
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_select_contacts.*


class SelectContactsFragment : Fragment() {

    data class SelectContactEvent(val contactResultList: List<ContactResult>,
                                  val currentContactModelList: List<ContactModel>)

    @Parcelize
    data class State(val state: String) : Parcelable

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

    private val viewModel = SelectContactsViewModel()

    companion object {

        @JvmStatic
        fun newInstance(): SelectContactsFragment {
            return SelectContactsFragment()
        }
    }

    // Fragment override methods

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_select_contacts,
                                container,
                                false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val state: State = savedInstanceState?.getParcelable("key") ?: State("")

        Logger.d(state.toString())

        setup()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {

        outState.putParcelable("key", State("state"))

        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()

        compositeDisposable.clear()
    }

    override fun onResume() {
        super.onResume()

        setupReactive()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CONTACT_PICKER_REQUEST) {
            when (resultCode) {
                RESULT_OK -> {
                    contactResult(MultiContactPicker.obtainResult(data))
                }
                RESULT_CANCELED ->
                    contactResult(emptyList())
            }
        }
    }

    // ------------------------------------------------------------------------------------------

    private fun setup() {
        setupActionBar()
        setupRecyclerView()
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

        compositeDisposable += select_contacts_next_skip_btn
                .clicks()
                .subscribe {  }
    }

    private fun contactResult(contactResultList: List<ContactResult>) {

        compositeDisposable += Observable.just(
                SelectContactEvent(contactResultList,
                                   listAdapter.getList()))
                .compose(viewModel.transformerContact)
                .subscribe { contactResult ->

                    with(listAdapter) {
                        clearList()
                        addAllToList(contactResult.contactModelList)
                        contactResult.diffResult.dispatchUpdatesTo(this)
                    }
                }
    }

}
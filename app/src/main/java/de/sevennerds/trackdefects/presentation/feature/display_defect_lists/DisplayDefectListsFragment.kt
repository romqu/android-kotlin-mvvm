package de.sevennerds.trackdefects.presentation.feature.display_defect_lists

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.TrackDefectsApp
import de.sevennerds.trackdefects.common.asObservable
import de.sevennerds.trackdefects.presentation.MainActivity
import de.sevennerds.trackdefects.presentation.base.BaseFragment
import de.sevennerds.trackdefects.presentation.feature.select_participants_defect_list.SelectParticipantsView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_display_defect_lists.*
import javax.inject.Inject

class DisplayDefectListsFragment : BaseFragment() {

    @Inject
    lateinit var viewModel: DisplayDefectListsViewModel

    private val initEventSubject: PublishSubject<SelectParticipantsView.Event.Init> =
            PublishSubject.create()

    private val compositeDisposable = CompositeDisposable()

    private var parcelState: SelectParticipantsView.ParcelState? = null

    private lateinit var listAdapter: DisplayDefectListsAdapter

    private var isRotation = false


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

        return inflater.inflate(R.layout.fragment_display_defect_lists,
                                container,
                                false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setup()
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
    // ---------------------------------------------------------------------------------------------


    private fun setup() {
        setupActionBar()
        setupRecyclerView()
        setupEvents()
    }

    private fun setupActionBar() {
        MainActivity[requireContext()].supportActionBar?.title = "Defect Lists"
    }

    private fun setupRecyclerView() {

        with(displayDefectListsRcv) {
            layoutManager = LinearLayoutManager(context)
            listAdapter = DisplayDefectListsAdapter(mutableListOf())
            adapter = listAdapter
        }
    }


    private fun setupEvents() {
        compositeDisposable += Observable
                .mergeArray(DisplayDefectListsView.Event.Init.asObservable())
                .compose(viewModel.eventToViewState)
                .subscribe(::render)
    }

    private fun render(viewState: DisplayDefectListsView.State) {

        val renderState = viewState.renderState

        return when (renderState) {

            is DisplayDefectListsView.RenderState.Init -> {
            }
            is DisplayDefectListsView.RenderState.None -> {
            }
        }
    }


}
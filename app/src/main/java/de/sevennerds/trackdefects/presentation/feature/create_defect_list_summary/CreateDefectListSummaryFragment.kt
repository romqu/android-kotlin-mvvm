package de.sevennerds.trackdefects.presentation.feature.create_defect_list_summary

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orhanobut.logger.Logger
import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.TrackDefectsApp
import de.sevennerds.trackdefects.common.asObservable
import de.sevennerds.trackdefects.presentation.base.BaseFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_create_defect_list_summary.*
import javax.inject.Inject

class CreateDefectListSummaryFragment : BaseFragment() {

    @Inject
    lateinit var viewModel: CreateDefectListSummaryViewModel

    private val compositeDisposable = CompositeDisposable()
    private var isRotation = false
    private var viewParcelState: CreateDefectListSummaryView.StateParcel? = null


    override fun onAttach(context: Context) {

        TrackDefectsApp
                .get(context)
                .appComponent
                .inject(this)

        super.onAttach(context)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            viewParcelState = it.getParcelable(KEY_STATE_PARCEL)
        }

    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_create_defect_list_summary,
                                container,
                                false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        createDefectListSummaryStreetAddressExtEditTxt.isEnabled = false

        setupEvents()
    }

    override fun onPause() {
        super.onPause()

        isRotation = true

        compositeDisposable.clear()
    }

    override fun onSaveInstanceState(outState: Bundle) {

        viewParcelState?.let {
            outState.putParcelable(KEY_STATE_PARCEL, it)
        }

        super.onSaveInstanceState(outState)
    }


    override fun onResume() {
        super.onResume()

        if (isRotation) {
            isRotation = false
            setupEvents()
        }
    }

    private fun setupEvents() {

        Observable
                .merge(CreateDefectListSummaryView
                               .Event
                               .Init
                               .asObservable(),
                       Observable.empty())
                .compose(viewModel.eventToViewState)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::render)
                .addTo(compositeDisposable)
    }

    private fun render(viewState: CreateDefectListSummaryView.State) =
            when (viewState.renderState) {

                is CreateDefectListSummaryView.RenderState.Init -> {

                    createDefectListSummaryStreetAddressExtEditTxt
                            .setText(viewState.renderState
                                             .defectListModel
                                             .streetAddressModel
                                             .name)

                    createDefectListSummaryParticipantsExtEditTxt
                            .setText(viewState
                                             .renderState
                                             .defectListModel
                                             .viewParticipantModelList
                                             .map { it.name }
                                             .reduce { acc, s ->
                                                 acc + s
                                             }

                            )

                    createDefectListSummaryGroundPlanImgView
                            .setImageBitmap(viewState.renderState
                                                    .defectListModel
                                                    .imageModel
                                                    .data)
                }

                is CreateDefectListSummaryView.RenderState.None -> {
                }
            }


}
package de.sevennerds.trackdefects.presentation.feature.preview_image

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.orhanobut.logger.Logger
import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.TrackDefectsApp
import de.sevennerds.trackdefects.common.asObservable
import de.sevennerds.trackdefects.presentation.MainActivity
import de.sevennerds.trackdefects.presentation.base.BaseFragment
import de.sevennerds.trackdefects.presentation.feature.create_defect_list_summary.navigation.CreateDefectListSummaryKey
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_preview_image.*
import javax.inject.Inject

class PreviewImageFragment : BaseFragment() {

    @Inject
    lateinit var viewModel: PreviewImageViewModel

    private val compositeDisposable = CompositeDisposable()

    private var isRotation = false

    private var viewStateParcel: PreviewImageView.StateParcel? = null


    override fun onAttach(context: Context?) {

        TrackDefectsApp.get(context!!)
                .appComponent
                .inject(this)

        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewStateParcel = savedInstanceState?.getParcelable(KEY_STATE_PARCEL)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        isRotation = false

        return inflater.inflate(R.layout.fragment_preview_image,
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

        viewStateParcel?.let {
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

    private fun setup() {
        setupEvents()
    }

    private fun setupEvents() {

        compositeDisposable += Observable
                .merge(PreviewImageView
                               .Event
                               .LoadImage
                               .asObservable(),
                       previewImageDismissBtn
                               .clicks()
                               .map { PreviewImageView.Event.DismissImage },
                       previewImageAcceptBtn
                               .clicks()
                               .map { PreviewImageView.Event.AcceptImage })
                .compose(viewModel.eventToViewState)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::render)
    }

    private fun render(viewState: PreviewImageView.State) =
            when (viewState.renderState) {

                is PreviewImageView.RenderState.LoadImage ->
                    previewImageImgView.setImageBitmap(viewState.renderState.bitmap)

                is PreviewImageView.RenderState.DismissImage -> {
                    MainActivity[requireContext()].onBackPressed()
                }

                is PreviewImageView.RenderState.AcceptImage -> {
                    MainActivity[requireContext()].replaceTopWith(CreateDefectListSummaryKey())
                }

                PreviewImageView.RenderState.None -> {
                }
            }
}
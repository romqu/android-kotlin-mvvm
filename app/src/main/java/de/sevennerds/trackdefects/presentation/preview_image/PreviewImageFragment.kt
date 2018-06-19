package de.sevennerds.trackdefects.presentation.preview_image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.TrackDefectsApp
import de.sevennerds.trackdefects.common.asObservable
import de.sevennerds.trackdefects.core.di.MessageQueue
import de.sevennerds.trackdefects.presentation.base.BaseFragment
import de.sevennerds.trackdefects.presentation.preview_image.navigation.PreviewImageKey
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_preview_image.*
import javax.inject.Inject

class PreviewImageFragment : BaseFragment(), MessageQueue.Receiver {

    private val KEY_STATE = "KEY_STATE"

    @Inject
    lateinit var messageQueue: MessageQueue
    @Inject
    lateinit var viewModel: PreviewImageViewModel

    private val compositeDisposable = CompositeDisposable()

    private var isRotation = false


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

        TrackDefectsApp.get(context!!)
                .appComponent
                .inject(this)

        setup()

        savedInstanceState?.apply {
            val state = getParcelable<PreviewImageView.StateParcel>(KEY_STATE)
            init(state.imageName)
        } ?: messageQueue.requestMessages(PreviewImageKey(),
                                          this)

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

    private fun setup() {
        setupEvents()
    }

    private fun setupEvents() {

        previewImageAcceptBtn
                .clicks()
                .subscribe()

        previewImageDismissBtn
                .clicks()
                .subscribe()

    }

    private fun init(imageName: String) {
        compositeDisposable += imageName.asObservable()
                .map { PreviewImageView.Event.LoadImage(it) }
                .compose(viewModel.eventTransformer)
                .subscribe(::render)
    }

    private fun render(renderState: PreviewImageView.RenderState) =
            when (renderState) {

                is PreviewImageView.RenderState.LoadImage ->
                    previewImageImgView.setImageBitmap(renderState.bitmap)
                is PreviewImageView.RenderState.DismissImage -> TODO()
                is PreviewImageView.RenderState.AcceptImage -> TODO()

            }

    override fun receiveMessage(message: MessageQueue.Message) {
        when (message) {
            is PreviewImageView.Message.ImageName ->
                compositeDisposable += message.imageName
                        .asObservable()
                        .map { PreviewImageView.Event.LoadImage(it) }
                        .compose(viewModel.eventTransformer)
                        .subscribe(::render)
            else -> {
            }
        }
    }
}
package de.sevennerds.trackdefects.presentation.feature.take_ground_plan_picture

import android.animation.ObjectAnimator
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.orhanobut.logger.Logger
import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.TrackDefectsApp
import de.sevennerds.trackdefects.core.di.MessageQueue
import de.sevennerds.trackdefects.presentation.MainActivity
import de.sevennerds.trackdefects.presentation.base.BaseFragment
import de.sevennerds.trackdefects.presentation.feature.preview_image.PreviewImageView
import de.sevennerds.trackdefects.presentation.feature.preview_image.navigation.PreviewImageKey
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.*
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_take_ground_plan_image.*
import javax.inject.Inject

class TakeGroundPlanPictureFragment : BaseFragment() {

    @Inject
    lateinit var viewModel: TakeGroundPlanPictureViewModel
    @Inject
    lateinit var messageQueue: MessageQueue
    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    /*private val camera by lazy {
        Fotoapparat(
                context = context!!,
                cameraConfiguration = cameraConfiguration,
                view = takeGroundPlanPictureTakCameraView,                   // view which will draw the camera preview
                scaleType = ScaleType.CenterCrop,    // (optional) we want the preview to fill the view
                lensPosition = back(),  // (optional) log fatal errors
                cameraErrorCallback = { cameraException -> Logger.d(cameraException.toString()) }
        )
    }*/

    private lateinit var camera: Fotoapparat

    private val cameraConfiguration = CameraConfiguration(
            pictureResolution = highestResolution(), // (optional) we want to have the highest possible photo resolution
            previewResolution = highestResolution(), // (optional) we want to have the highest possible preview resolution
            previewFpsRange = highestFps(),          // (optional) we want to have the best frame rate
            focusMode = firstAvailable(              // (optional) use the first focus mode which is supported by device
                    continuousFocusPicture(),
                    autoFocus()                          // if even auto focus is not available - fixed focus mode will be used
            ),
            jpegQuality = manualJpegQuality(85)
    )

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_take_ground_plan_image,
                                container,
                                false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        TrackDefectsApp.get(context!!)
                .appComponent
                .inject(this)

        ObjectAnimator.ofFloat(takeGroundPlanPictureTakePictureBtn, "", 360F)
                .start()

        camera = Fotoapparat(
                context = context!!,
                cameraConfiguration = cameraConfiguration,
                view = takeGroundPlanPictureTakCameraView,                   // view which will draw the camera preview
                scaleType = ScaleType.CenterCrop,    // (optional) we want the preview to fill the view
                lensPosition = back(),  // (optional) log fatal errors
                cameraErrorCallback = { cameraException -> Logger.d(cameraException.toString()) }
        )
    }

    override fun onStart() {
        super.onStart()

        MainActivity[context!!].apply {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            supportActionBar?.hide()
            requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
        }

        camera.start()

        setupEvents()
    }

    override fun onPause() {
        super.onPause()

        compositeDisposable.clear()
    }

    override fun onStop() {
        super.onStop()

        MainActivity[context!!].apply {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            supportActionBar?.show()
            requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }

        camera.stop()
    }

    private fun setupEvents() {


        compositeDisposable += takeGroundPlanPictureTakePictureBtn
                .clicks()
                .flatMap {

                    Observable.fromCallable {
                        TakeGroundPlanPictureView.Event.TakePicture(
                                camera.takePicture())
                    }.subscribeOn(Schedulers.io())

                }
                .compose(viewModel.eventTransformer)
                .subscribe(::render)
    }

    private fun render(renderState: TakeGroundPlanPictureView.RenderState) =
            when (renderState) {

                is TakeGroundPlanPictureView.RenderState.TakePicture -> {
                    messageQueue
                            .pushMessageTo(PreviewImageKey(),
                                           PreviewImageView
                                                   .Message
                                                   .ImageName(renderState.imageName))

                    MainActivity[context!!].navigateTo(PreviewImageKey())
                }

            }


}
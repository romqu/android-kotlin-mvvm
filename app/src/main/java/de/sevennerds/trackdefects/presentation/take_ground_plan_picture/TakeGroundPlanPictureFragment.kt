package de.sevennerds.trackdefects.presentation.take_ground_plan_picture

import android.animation.ObjectAnimator
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.orhanobut.logger.Logger
import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.presentation.MainActivity
import de.sevennerds.trackdefects.presentation.base.BaseFragment
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.*
import kotlinx.android.synthetic.main.fragment_take_ground_plan_picture.*

class TakeGroundPlanPictureFragment : BaseFragment() {

    lateinit var viewModel: TakeGroundPlanPictureViewModel

    private val cameraConfiguration = CameraConfiguration(
            pictureResolution = highestResolution(),
            previewResolution = highestResolution(),
            previewFpsRange = highestFps(),
            focusMode = firstAvailable(
                    continuousFocusPicture(),
                    autoFocus()
            ),
            jpegQuality = manualJpegQuality(85)
    )

    private val camera by lazy {
        Fotoapparat(
                context = context!!,
                cameraConfiguration = cameraConfiguration,
                view = takeGroundPlanPictureCameraView,
                scaleType = ScaleType.CenterCrop,
                lensPosition = back(),
                cameraErrorCallback = { cameraException ->
                    Logger.e(cameraException.toString())
                }
        )
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_take_ground_plan_picture,
                                container,
                                false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = TakeGroundPlanPictureViewModel(
                TakeGroundPlanPictureView.State.initial())

        ObjectAnimator.ofFloat(takeGroundPlanPictureTakePictureBtn, "", 360F)
                .start()

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

    fun setupEvents() {

        takeGroundPlanPictureTakePictureBtn
                .clicks()
                .map {
                    TakeGroundPlanPictureView.Event.TakePicture(camera.takePicture())
                }
                .compose(viewModel.eventTransformer)
                .subscribe()
    }


}
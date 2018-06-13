package de.sevennerds.trackdefects.presentation.take_ground_plan_image

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
import io.fotoapparat.result.adapter.rxjava2.toObservable
import io.fotoapparat.selector.*
import kotlinx.android.synthetic.main.fragment_take_ground_plan_image.*

class TakeGroundPlanImageFragment : BaseFragment() {


    private val camera by lazy {
        Fotoapparat(
                context = context!!,
                cameraConfiguration = cameraConfiguration,
                view = camera_view,                   // view which will draw the camera preview
                scaleType = ScaleType.CenterCrop,    // (optional) we want the preview to fill the view
                lensPosition = back(),  // (optional) log fatal errors
                cameraErrorCallback = { cameraException -> Logger.d(cameraException.toString()) }
        )
    }

    private val cameraConfiguration = CameraConfiguration(
            pictureResolution = highestResolution(), // (optional) we want to have the highest possible photo resolution
            previewResolution = highestResolution(), // (optional) we want to have the highest possible preview resolution
            previewFpsRange = highestFps(),          // (optional) we want to have the best frame rate
            focusMode = firstAvailable(              // (optional) use the first focus mode which is supported by device
                    continuousFocusPicture(),
                    autoFocus()                          // if even auto focus is not available - fixed focus mode will be used
            ),
            jpegQuality = manualJpegQuality(90)
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

        val main = MainActivity[context!!]

        val decorView = main.window.decorView

        camera_view.setOnTouchListener { _, _ ->
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            true
        }

        ObjectAnimator.ofFloat(camera_button, "", 360F)
                .start()

    }

    override fun onStart() {
        super.onStart()

        val main = MainActivity[context!!]

        val decorView = main.window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        main.supportActionBar?.hide()

        main.requestedOrientation =
                ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE

        camera.start()
    }

    override fun onStop() {
        super.onStop()

        val main = MainActivity[context!!]

        val decorView = main.window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE

        main.supportActionBar?.show()

        main.requestedOrientation =
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        camera.stop()
    }

    fun setupEvents() {
        camera_button.clicks()
                .flatMap {
                    camera.takePicture()
                            .toBitmap()
                            .toObservable()
                }.subscribe()
    }


}
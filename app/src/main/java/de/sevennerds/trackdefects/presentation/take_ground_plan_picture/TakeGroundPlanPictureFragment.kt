package de.sevennerds.trackdefects.presentation.take_ground_plan_picture

import android.animation.ObjectAnimator
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.orhanobut.logger.Logger
import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.presentation.MainActivity
import de.sevennerds.trackdefects.presentation.base.BaseFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_take_ground_plan_image.*

class TakeGroundPlanPictureFragment : BaseFragment() {

    private lateinit var viewModel: TakeGroundPlanPictureViewModel
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

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

        viewModel = TakeGroundPlanPictureViewModel(
                TakeGroundPlanPictureView.State.initial())

        ObjectAnimator.ofFloat(takeGroundPlanPictureTakePictureBtn, "", 360F)
                .start()

        setupEvents()

    }

    override fun onStart() {
        super.onStart()

        MainActivity[context!!].apply {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            supportActionBar?.hide()
            requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
        }

        // camera.start()
    }

    override fun onPause() {
        super.onPause()

        // compositeDisposable.clear()
    }

    override fun onStop() {
        super.onStop()

        MainActivity[context!!].apply {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            supportActionBar?.show()
            requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }

        // camera.stop()
    }

    private fun setupEvents() {


        compositeDisposable += takeGroundPlanPictureTakePictureBtn
                .clicks()
                .doOnNext { Logger.d("NEXT") }
                .map {
                    TakeGroundPlanPictureView
                            .Event
                            .TakePicture(
                                    Bitmap.createBitmap(10,
                                                        10,
                                                        Bitmap.Config.ARGB_8888))
                }
                .compose(viewModel.eventTransformer)

                .doOnError { Logger.e(it.toString()) }
                .subscribe { Logger.d("SUB") }
    }


}
package de.sevennerds.trackdefects.presentation.take_ground_plan_picture

import io.reactivex.ObservableTransformer

class TakeGroundPlanPictureViewModel(private var viewState: TakeGroundPlanPictureView.State) {

    private val takePictureEventTransformer =
            ObservableTransformer<TakeGroundPlanPictureView.Event.TakePicture,
                    TakeGroundPlanPictureView.RenderState.TakePicture> { upstream ->

                upstream.flatMap { takePictureEvent ->

                }
            }

    private val resultTransformer =
            ObservableTransformer<TakeGroundPlanPictureView.Result,
                    TakeGroundPlanPictureView.State> { upstream ->

                upstream.scan(viewState) { _, result ->
                    when (result) {

                        is TakeGroundPlanPictureView.Result.TakePicture -> viewState
                    }
                }.skip(1)
            }

}
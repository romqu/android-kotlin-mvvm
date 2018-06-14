package de.sevennerds.trackdefects.presentation.take_ground_plan_picture

import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class TakeGroundPlanPictureViewModel(private var viewState: TakeGroundPlanPictureView.State) {

    private val eventTransformer = ObservableTransformer<TakeGroundPlanPictureView.Event,
            TakeGroundPlanPictureView.RenderState> { upstream ->

        upstream.publish { shared ->
            Observable
                    .merge(shared.ofType(TakeGroundPlanPictureView.Event.TakePicture::class.java)
                                   .compose(takePictureEventTransformer),
                           Observable.empty())
        }

    }

    private val takePictureEventTransformer =
            ObservableTransformer<TakeGroundPlanPictureView.Event.TakePicture,
                    TakeGroundPlanPictureView.RenderState> { upstream ->

                upstream.map { takePictureEvent ->
                    TakeGroundPlanPictureView.RenderState.TakePicture("")
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
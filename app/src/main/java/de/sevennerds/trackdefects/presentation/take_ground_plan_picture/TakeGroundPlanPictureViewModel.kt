package de.sevennerds.trackdefects.presentation.take_ground_plan_picture

import android.graphics.Bitmap
import androidx.collection.LruCache
import de.sevennerds.trackdefects.common.applySchedulers
import de.sevennerds.trackdefects.util.getUuidV4
import io.fotoapparat.result.adapter.rxjava2.toObservable
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class TakeGroundPlanPictureViewModel @Inject constructor(
        private val bitmapCache: LruCache<String, Bitmap>) {

    private var viewState: TakeGroundPlanPictureView.State = TakeGroundPlanPictureView.State.initial()

    val eventTransformer = ObservableTransformer<TakeGroundPlanPictureView.Event,
            TakeGroundPlanPictureView.RenderState> { upstream ->

        upstream.ofType(TakeGroundPlanPictureView.Event.TakePicture::class.java)
                .compose(takePictureEventTransformer)

        /*upstream.publish { shared ->
            Observable
                    .merge(shared.ofType(TakeGroundPlanPictureView.Event.TakePicture::class.java)
                                   .compose(takePictureEventTransformer),
                           Observable.empty())*/


    }

    private val takePictureEventTransformer =
            ObservableTransformer<TakeGroundPlanPictureView.Event.TakePicture,
                    TakeGroundPlanPictureView.RenderState> { upstream ->

                upstream.flatMap { takePictureEvent ->
                    takePictureEvent
                            .photoResult
                            .toBitmap()
                            .toObservable()
                            .map { bitmapPhoto ->
                                val imageName = getUuidV4()
                                bitmapCache.put(imageName, bitmapPhoto.bitmap)
                                TakeGroundPlanPictureView.Result.TakePicture(imageName)
                            }
                            .compose(resultTransformer)
                            .map { viewState ->
                                TakeGroundPlanPictureView.RenderState.TakePicture(viewState.imageName)
                            }.applySchedulers()
                }
            }

    private val resultTransformer =
            ObservableTransformer<TakeGroundPlanPictureView.Result,
                    TakeGroundPlanPictureView.State> { upstream ->

                upstream.scan(viewState) { _, result ->
                    when (result) {

                        is TakeGroundPlanPictureView.Result.TakePicture -> viewState.copy(result.imageName)
                    }
                }.skip(1)
            }

}
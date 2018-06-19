package de.sevennerds.trackdefects.presentation.preview_image

import android.graphics.Bitmap
import androidx.collection.LruCache
import de.sevennerds.trackdefects.presentation.model.FileModel
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class PreviewImageViewModel @Inject constructor(
        private val bitmapCache: LruCache<String, Bitmap>) {

    private var viewState: PreviewImageView.State = PreviewImageView.State.initial()


    val eventTransformer = ObservableTransformer<PreviewImageView.Event,
            PreviewImageView.RenderState> { upstream ->

        upstream.publish { shared ->
            Observable
                    .merge(shared.ofType(PreviewImageView.Event.LoadImage::class.java)
                                   .compose(loadImageEventTransformer),
                           Observable.empty())
        }


    }

    private val loadImageEventTransformer =
            ObservableTransformer<PreviewImageView.Event.LoadImage,
                    PreviewImageView.RenderState> { upstream ->

                upstream
                        .map { loadImageEvent ->
                            PreviewImageView.Result.LoadImage(
                                    FileModel(loadImageEvent.imageName,
                                              bitmapCache.get(loadImageEvent.imageName)))
                        }.compose(resultTransformer)
                        .map { viewState ->
                            PreviewImageView.RenderState.LoadImage(viewState.image.data)
                        }
            }

    private val resultTransformer =
            ObservableTransformer<PreviewImageView.Result,
                    PreviewImageView.State> { upstream ->

                upstream.scan(viewState) { previousState, result ->
                    when (result) {

                        is PreviewImageView.Result.LoadImage -> {
                            viewState = previousState.copy(image = result.image)
                            viewState
                        }
                        is PreviewImageView.Result.DismissImage -> TODO()
                        is PreviewImageView.Result.AcceptImage -> TODO()
                    }
                }.skip(1)
            }

    fun getViewStateParcel() =
            PreviewImageView.StateParcel(viewState.image.name)

}
package de.sevennerds.trackdefects.presentation.feature.preview_image

import android.graphics.Bitmap
import androidx.collection.LruCache
import de.sevennerds.trackdefects.presentation.base.BaseViewModel
import de.sevennerds.trackdefects.presentation.model.FileModel
import de.sevennerds.trackdefects.presentation.realm_db.CreateBasicDefectListSummaryRealm
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.kotlin.where
import javax.inject.Inject

class PreviewImageViewModel @Inject constructor(
        private val bitmapCache: LruCache<String, Bitmap>)
    : BaseViewModel<PreviewImageView.Event, PreviewImageView.State>() {


    override val eventToViewState =
            ObservableTransformer<PreviewImageView.Event, PreviewImageView.State> { upstream ->

                upstream.observeOn(Schedulers.io())
                        .publish { shared ->
                            Observable.merge(
                                    shared.ofType(PreviewImageView.Event.LoadImage::class.java)
                                            .compose(eventLoadImageToResult),
                                    shared.ofType(PreviewImageView.Event.DismissImage::class.java)
                                            .compose(eventDismissImageToResult),
                                    shared.ofType(PreviewImageView.Event.AcceptImage::class.java)
                                            .compose(eventAcceptImageToResult))
                                    .compose(resultToViewState)
                        }
            }

    private val eventLoadImageToResult = ObservableTransformer<PreviewImageView.Event.LoadImage,
            PreviewImageView.Result> { upstream ->

        upstream.flatMap {
            Realm.getDefaultInstance().use {
                it.where<CreateBasicDefectListSummaryRealm>()
                        .equalTo("id", 0L)
                        .findAllAsync()
                        .asFlowable()
                        .toObservable()

            }.map {

                val realmObject = it.first()!!

                val bitmap = bitmapCache.get(realmObject.groundPlanPictureName)

                PreviewImageView.Result.LoadImage(
                        FileModel(realmObject.groundPlanPictureName,
                                  bitmap))
            }
        }
    }

    private val eventAcceptImageToResult = ObservableTransformer<PreviewImageView.Event.AcceptImage,
            PreviewImageView.Result> { upstream ->

        upstream.map { PreviewImageView.Result.AcceptImage }
    }

    private val eventDismissImageToResult = ObservableTransformer<PreviewImageView.Event.DismissImage,
            PreviewImageView.Result> { upstream ->

        upstream.map { PreviewImageView.Result.DismissImage }
    }

    private val resultToViewState =
            ObservableTransformer<PreviewImageView.Result,
                    PreviewImageView.State> { upstream ->

                upstream.scan(PreviewImageView.State.initial()) { previousState,
                                                                  result ->
                    when (result) {

                        is PreviewImageView.Result.LoadImage -> {

                            previousState.copy(
                                    renderState = PreviewImageView.RenderState.LoadImage(result.image.data))
                        }

                        is PreviewImageView.Result.DismissImage ->
                            previousState

                        is PreviewImageView.Result.AcceptImage ->
                            previousState
                    }
                }.skip(1)
            }
}

package de.sevennerds.trackdefects.presentation.feature.take_ground_plan_picture

import android.graphics.Bitmap
import androidx.collection.LruCache
import com.vicpin.krealmextensions.queryFirst
import de.sevennerds.trackdefects.domain.feature.save_picture_temporary.SavePictureTemporaryTask
import de.sevennerds.trackdefects.presentation.base.BaseViewModel
import de.sevennerds.trackdefects.presentation.model.FileModel
import de.sevennerds.trackdefects.presentation.realm_db.CreateBasicDefectListSummaryRealm
import de.sevennerds.trackdefects.presentation.realm_db.RealmManager
import io.fotoapparat.result.adapter.rxjava2.toObservable
import io.fotoapparat.result.transformer.scaled
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TakeGroundPlanPictureViewModel @Inject constructor(
        private val savePictureTemporaryTask: SavePictureTemporaryTask)
    : BaseViewModel<TakeGroundPlanPictureView.Event,
        TakeGroundPlanPictureView.State>() {


    private var viewState: TakeGroundPlanPictureView.State = TakeGroundPlanPictureView.State.initial()

    override val eventToViewState = ObservableTransformer<TakeGroundPlanPictureView.Event,
            TakeGroundPlanPictureView.State> { upstream ->

        upstream
                .observeOn(Schedulers.io())
                .publish { shared ->
                    Observable.merge(shared.ofType(TakeGroundPlanPictureView.Event.TakePicture::class.java)
                                             .compose(evenTakePictureToResult),
                                     Observable.empty())
                }
                .compose(resultToViewState)
    }

    private val evenTakePictureToResult =
            ObservableTransformer<TakeGroundPlanPictureView.Event.TakePicture,
                    TakeGroundPlanPictureView.Result> { upstream ->

                upstream.flatMap { takePictureEvent ->
                    takePictureEvent
                            .photoResult
                            .toBitmap(scaled(scaleFactor = 0.25f))
                            .toObservable()
                }
                        .flatMapSingle { bitmapPhoto ->
                            /*val imageName = getUuidV4()
                            bitmapCache.put(imageName, bitmapPhoto.bitmap)
                            TakeGroundPlanPictureView.Result.TakePicture(imageName)*/

                            savePictureTemporaryTask
                                    .execute(FileModel("", bitmapPhoto.bitmap))
                                    .map {
                                        it.match({ data ->
                                                     TakeGroundPlanPictureView.Result.TakePicture(data.name)
                                                 },
                                                 { _ ->
                                                     TakeGroundPlanPictureView.Result.TakePicture("")
                                                 })
                                    }


                        }
            }

    private val resultToViewState =
            ObservableTransformer<TakeGroundPlanPictureView.Result,
                    TakeGroundPlanPictureView.State> { upstream ->

                upstream.scan(TakeGroundPlanPictureView.State.initial()) { previousState,
                                                                           result ->
                    when (result) {

                        is TakeGroundPlanPictureView.Result.TakePicture -> {

                            val state = previousState.copy(imageName = result.imageName)

                            // TODO impure
                            updateSharedRealmObject(state)

                            state.copy(renderState = TakeGroundPlanPictureView.RenderState.TakePicture)
                        }
                    }
                }.skip(1)
            }


    // TODO impure
    private fun updateSharedRealmObject(viewState: TakeGroundPlanPictureView.State) {

        val createBasicDefectListSummaryRealm = queryFirst<CreateBasicDefectListSummaryRealm>()!!

        createBasicDefectListSummaryRealm.groundPlanPictureName = viewState.imageName

        RealmManager.insertOrUpdate(createBasicDefectListSummaryRealm)
    }

}
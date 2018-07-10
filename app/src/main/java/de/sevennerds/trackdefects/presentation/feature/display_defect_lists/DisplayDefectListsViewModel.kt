package de.sevennerds.trackdefects.presentation.feature.display_defect_lists

import android.graphics.Bitmap
import androidx.collection.LruCache
import com.vicpin.krealmextensions.queryAsSingle
import de.sevennerds.trackdefects.presentation.base.BaseViewModel
import de.sevennerds.trackdefects.presentation.model.DefectListModel
import de.sevennerds.trackdefects.presentation.model.FileModel
import de.sevennerds.trackdefects.presentation.model.StreetAddressModel
import de.sevennerds.trackdefects.presentation.model.ViewParticipantModel
import de.sevennerds.trackdefects.presentation.realm_db.CreateBasicDefectListSummaryRealm
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DisplayDefectListsViewModel @Inject constructor(
        private val bitmapCache: LruCache<String, Bitmap>)
    : BaseViewModel<DisplayDefectListsView.Event, DisplayDefectListsView.State>() {

    override val eventToViewState = ObservableTransformer<DisplayDefectListsView.Event,
            DisplayDefectListsView.State> { upstream ->

        upstream.observeOn(Schedulers.io())
                .publish { shared ->
                    Observable.mergeArray(shared.ofType(
                            DisplayDefectListsView.Event.Init::class.java)
                                                  .compose(initEventToResult))
                            .compose(resultToViewState)
                }

    }


    private val initEventToResult = ObservableTransformer<DisplayDefectListsView.Event.Init,
            DisplayDefectListsView.Result> { upstream ->

        upstream.flatMap {

            queryAsSingle<CreateBasicDefectListSummaryRealm> {
                equalTo("id", 0L)
            }
                    .toObservable()
        }
                .map { it.first() }
                .map { createBasicDefectListRealm ->
                    val groundPlanPictureName = createBasicDefectListRealm.groundPlanPictureName
                    val streetAddressRealm = createBasicDefectListRealm.streetAddressRealm
                    val viewParticipantRealmList = createBasicDefectListRealm.viewParticipantRealmList

                    val bitmap = bitmapCache.get(groundPlanPictureName)
                    val imageModel = FileModel<Bitmap>(groundPlanPictureName, bitmap!!)
                    val streetAddressModel = with(streetAddressRealm!!) {
                        StreetAddressModel(streetName, streetNumber, streetAdditional)
                    }


                    val viewParticipantModelList = viewParticipantRealmList
                            .map {
                                with(it!!) {
                                    ViewParticipantModel(name, email, phoneNumber)
                                }
                            }

                    listOf(DefectListModel(
                            "name",
                            imageModel,
                            streetAddressModel,
                            viewParticipantModelList))
                }
                .map { DisplayDefectListsView.Result.Init(it) }

    }

    private val resultToViewState = ObservableTransformer<DisplayDefectListsView.Result,
            DisplayDefectListsView.State> { upstream ->

        upstream.scan(DisplayDefectListsView.State.initial()) { previousState,
                                                                result ->

            when (result) {
                is DisplayDefectListsView.Result.Init ->
                    previousState.copy(renderState = DisplayDefectListsView
                            .RenderState.Init(result.defectListModelList))
            }

        }

    }

}
package de.sevennerds.trackdefects.presentation.feature.create_defect_list_summary

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

class CreateDefectListSummaryViewModel @Inject constructor(
        private val bitmapCache: LruCache<String, Bitmap>)
    : BaseViewModel<CreateDefectListSummaryView.Event,
        CreateDefectListSummaryView.State>() {

    override val eventToViewState = ObservableTransformer<CreateDefectListSummaryView.Event,
            CreateDefectListSummaryView.State> { upstream ->

        upstream.observeOn(Schedulers.io())
                .publish { shared ->

                    Observable.merge(
                            shared.ofType(CreateDefectListSummaryView.Event.Init::class.java)
                                    .compose(eventInitToResult),
                            Observable.empty())
                            .compose(resultToViewState)
                }
    }

    private val eventInitToResult = ObservableTransformer<CreateDefectListSummaryView.Event.Init,
            CreateDefectListSummaryView.Result> { upstream ->

        upstream
                .flatMap {

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
                    val imageModel = FileModel<Bitmap>(groundPlanPictureName, bitmap)
                    val streetAddressModel = with(streetAddressRealm!!) {
                        StreetAddressModel(streetName, streetNumber, streetAdditional)
                    }

                    val viewParticipantModelList = viewParticipantRealmList
                            .map {
                                with(it!!) {
                                    ViewParticipantModel(name, email, phoneNumber)
                                }
                            }

                    DefectListModel("name", imageModel, streetAddressModel, viewParticipantModelList)
                }
                .map { CreateDefectListSummaryView.Result.Init(it) }

    }

    private val resultToViewState = ObservableTransformer<CreateDefectListSummaryView.Result,
            CreateDefectListSummaryView.State> { upstream ->

        upstream.scan(CreateDefectListSummaryView.State.initial()) { previousState,
                                                                     result ->

            when (result) {
                is CreateDefectListSummaryView.Result.Init ->
                    previousState.copy(renderState = CreateDefectListSummaryView.RenderState.Init(result.defectListModel))
            }

        }
    }


}
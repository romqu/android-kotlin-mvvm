package de.sevennerds.trackdefects.presentation.feature.create_defect_list_summary

import android.graphics.Bitmap
import com.vicpin.krealmextensions.queryAsSingle
import de.sevennerds.trackdefects.common.Constants
import de.sevennerds.trackdefects.domain.feature.create_basic_defect_list.CreateBasicDefectListManager
import de.sevennerds.trackdefects.domain.feature.load_temporary_picture.LoadTemporaryPictureTask
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
        private val loadTemporaryPictureTask: LoadTemporaryPictureTask,
        private val createBasicDefectListManager: CreateBasicDefectListManager)
    : BaseViewModel<CreateDefectListSummaryView.Event,
        CreateDefectListSummaryView.State>() {

    private class InitEvent(
            private val createBasicDefectListSummaryRealm: CreateBasicDefectListSummaryRealm,
            private val fileModel: FileModel<Bitmap>)

    override val eventToViewState = ObservableTransformer<CreateDefectListSummaryView.Event,
            CreateDefectListSummaryView.State> { upstream ->

        upstream.observeOn(Schedulers.io())
                .publish { shared ->

                    Observable.merge(
                            shared.ofType(CreateDefectListSummaryView.Event.Init::class.java)
                                    .compose(eventInitToResult),
                            shared.ofType(CreateDefectListSummaryView.Event.Save::class.java)
                                    .compose(eventSaveToResult))
                            .compose(resultToViewState)
                }
    }

    private val eventInitToResult = ObservableTransformer<CreateDefectListSummaryView.Event.Init,
            CreateDefectListSummaryView.Result> { upstream ->

        upstream.flatMapSingle {

            queryAsSingle<CreateBasicDefectListSummaryRealm> {
                equalTo(Constants.REALM_OBJECT_ID,
                        Constants.REALM_OBJECT_ID_DEFAULT_VALUE)
            }
        }
                .map { it.first() }
                .flatMapSingle { createBasicDefectListRealm ->
                    val groundPlanPictureName = createBasicDefectListRealm.groundPlanPictureName
                    val streetAddressRealm = createBasicDefectListRealm.streetAddressRealm
                    val viewParticipantRealmList = createBasicDefectListRealm.viewParticipantRealmList

                    loadTemporaryPictureTask
                            .execute(groundPlanPictureName)
                            .map { result ->

                                result.match(
                                        { file ->
                                            val imageModel = FileModel(groundPlanPictureName, file.data)
                                            val streetAddressModel = with(streetAddressRealm!!) {
                                                StreetAddressModel(streetName, streetNumber, streetAdditional)
                                            }

                                            val viewParticipantModelList = viewParticipantRealmList
                                                    .map {
                                                        with(it!!) {
                                                            ViewParticipantModel(name, email, phoneNumber)
                                                        }
                                                    }

                                            val model = DefectListModel(
                                                    "name",
                                                    imageModel,
                                                    streetAddressModel,
                                                    viewParticipantModelList)

                                            CreateDefectListSummaryView.Result.Init(model)
                                        },
                                        { _ ->
                                            CreateDefectListSummaryView.Result.InitError

                                        })

                            }
                }
    }

    private val eventSaveToResult = ObservableTransformer<CreateDefectListSummaryView.Event.Save,
            CreateDefectListSummaryView.Result> { upstream ->

        upstream
                .flatMapSingle {

                    queryAsSingle<CreateBasicDefectListSummaryRealm> {
                        equalTo(Constants.REALM_OBJECT_ID,
                                Constants.REALM_OBJECT_ID_DEFAULT_VALUE)
                    }
                }
                .map { it.first() }
                .flatMapSingle { createBasicDefectListRealm ->
                    val groundPlanPictureName = createBasicDefectListRealm.groundPlanPictureName
                    val streetAddressRealm = createBasicDefectListRealm.streetAddressRealm
                    val viewParticipantRealmList = createBasicDefectListRealm.viewParticipantRealmList

                    loadTemporaryPictureTask
                            .execute(groundPlanPictureName)
                            .map { result ->

                                result.onSuccessResult { file ->
                                    val imageModel = FileModel(groundPlanPictureName, file.data)
                                    val streetAddressModel = with(streetAddressRealm!!) {
                                        StreetAddressModel(streetName, streetNumber, streetAdditional)
                                    }

                                    val viewParticipantModelList = viewParticipantRealmList
                                            .map {
                                                with(it!!) {
                                                    ViewParticipantModel(name, email, phoneNumber)
                                                }
                                            }

                                    DefectListModel(
                                            "name",
                                            imageModel,
                                            streetAddressModel,
                                            viewParticipantModelList)
                                }

                            }
                            .flatMap { result ->

                                result.onSuccessSingle { defectListModel ->
                                    createBasicDefectListManager
                                            .execute(defectListModel)
                                }

                            }
                            .map { result ->
                                result.match(
                                        {
                                            CreateDefectListSummaryView.Result.Save
                                        }, {
                                            CreateDefectListSummaryView.Result.SaveError
                                        })
                            }
                }

    }

    private val resultToViewState = ObservableTransformer<CreateDefectListSummaryView.Result,
            CreateDefectListSummaryView.State> { upstream ->

        upstream.scan(CreateDefectListSummaryView.State.initial()) { previousState,
                                                                     result ->

            when (result) {
                is CreateDefectListSummaryView.Result.Init ->

                    previousState.copy(
                            renderState = CreateDefectListSummaryView.RenderState.Init(result.defectListModel))
                is CreateDefectListSummaryView.Result.InitError ->
                    previousState.copy(renderState = CreateDefectListSummaryView.RenderState.None)
                is CreateDefectListSummaryView.Result.Save ->
                    previousState.copy(renderState = CreateDefectListSummaryView.RenderState.None)
                is CreateDefectListSummaryView.Result.SaveError ->
                    previousState.copy(renderState = CreateDefectListSummaryView.RenderState.None)
            }

        }
    }


}
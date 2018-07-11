package de.sevennerds.trackdefects.domain.feature.create_basic_defect_list

import de.sevennerds.trackdefects.data.defect_list.DefectListEntity
import de.sevennerds.trackdefects.data.defect_list.DefectListRepository
import de.sevennerds.trackdefects.data.floor_plan.FloorPlanEntity
import de.sevennerds.trackdefects.data.response.Result
import de.sevennerds.trackdefects.data.street_address.StreetAddressEntity
import de.sevennerds.trackdefects.data.view_participant.ViewParticipantEntity
import de.sevennerds.trackdefects.presentation.model.DefectListModel
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateBasicDefectListTask @Inject constructor(
        private val defectListRepository: DefectListRepository) {

    fun execute(defectListModel: DefectListModel): Single<Result<DefectListEntity>> {

        val defectListEntity = with(defectListModel) {
            val streetAddressEntity = with(streetAddressModel) {
                StreetAddressEntity(0,
                                    0,
                                    0,
                                    name, "",
                                    0,
                                    number.toInt(),
                                    additional, "")
            }

            val viewParticipantEntityList = viewParticipantModelList
                    .map { viewParticipantModel ->
                        ViewParticipantEntity(0,
                                              0,
                                              0,
                                              viewParticipantModel.name,
                                              viewParticipantModel.name,
                                              viewParticipantModel.phoneNumber.toInt(),
                                              viewParticipantModel.email,
                                              "")
                    }


            val floorPlanEntity = with(imageModel) {
                FloorPlanEntity(0,
                                imageModel.name,
                                0)
            }


            DefectListEntity("",
                             name,
                             floorPlanEntity,
                             streetAddressEntity,
                             viewParticipantEntityList)

        }

        return defectListRepository
                .insert(defectListEntity)
    }
}
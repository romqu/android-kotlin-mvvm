package de.sevennerds.trackdefects.domain.feature.create_basic_defect_list

import de.sevennerds.trackdefects.common.Constants
import de.sevennerds.trackdefects.data.defect_list.DefectListEntity
import de.sevennerds.trackdefects.data.defect_list.DefectListRepository
import de.sevennerds.trackdefects.data.file.FileRepository
import de.sevennerds.trackdefects.data.floor_plan.FloorPlanEntity
import de.sevennerds.trackdefects.data.response.Result
import de.sevennerds.trackdefects.data.street_address.StreetAddressEntity
import de.sevennerds.trackdefects.data.view_participant.ViewParticipantEntity
import de.sevennerds.trackdefects.domain.feature.save_picture_disk.SavePictureDiskTask
import de.sevennerds.trackdefects.presentation.model.DefectListModel
import de.sevennerds.trackdefects.presentation.model.FileModel
import de.sevennerds.trackdefects.util.getUuidV4
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateBasicDefectListManager @Inject constructor(
        private val defectListRepository: DefectListRepository,
        private val fileRepository: FileRepository,
        private val savePictureTask: SavePictureDiskTask) {

    fun execute(defectListModel: DefectListModel): Single<Result<DefectListEntity>> {

        // TODO into mapper class
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
                                              viewParticipantModel.phoneNumber,
                                              viewParticipantModel.email,
                                              "")
                    }


            val floorPlanEntity = with(floorPlanPicture) {
                FloorPlanEntity(0,
                                getUuidV4(),
                                0)
            }


            DefectListEntity("",
                             getUuidV4(),
                             floorPlanEntity,
                             streetAddressEntity,
                             viewParticipantEntityList)

        }

        return defectListRepository
                .insert(defectListEntity)
                .flatMap { result ->

                    result.onSuccessSingle { defectListEntity ->
                        fileRepository.createDirectory(
                                "${Constants.PROJECTS_PATH}/${defectListEntity.name}")

                    }

                }
                .flatMap { result ->
                    result.onSuccessSingle {
                        savePictureTask.execute(FileModel(
                                defectListEntity.floorPlanEntity!!.fileName,
                                defectListModel.floorPlanPicture.data,
                                "${Constants.PROJECTS_PATH}/${defectListEntity.name}"))
                    }
                }
                .map { Result.success(defectListEntity) }
    }
}
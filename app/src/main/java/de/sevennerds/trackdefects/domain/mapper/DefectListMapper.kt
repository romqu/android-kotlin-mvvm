package de.sevennerds.trackdefects.domain.mapper

import android.graphics.Bitmap
import de.sevennerds.trackdefects.data.defect_list.DefectListEntity
import de.sevennerds.trackdefects.data.file.FileEntity
import de.sevennerds.trackdefects.presentation.model.DefectListModel
import de.sevennerds.trackdefects.presentation.model.FileModel
import de.sevennerds.trackdefects.presentation.model.StreetAddressModel
import de.sevennerds.trackdefects.presentation.model.ViewParticipantModel

object DefectListMapper {

    fun entityToModel(defectListEntity: DefectListEntity, fileEntity: FileEntity<Bitmap>) =
            with(defectListEntity) {
                val streetAddressModel = streetAddressEntity?.run {
                    StreetAddressModel(name, number.toString(), additional)
                }

                val viewParticipantModelList = viewParticipantEntityList
                        .map { viewParticipantEntity ->
                            with(viewParticipantEntity) {
                                ViewParticipantModel(name, email, phoneNumber)
                            }
                        }

                val fileModel = floorPlanEntity?.run {
                    FileModel(name, fileEntity.data)
                }

                DefectListModel(name,
                                fileModel!!,
                                streetAddressModel!!,
                                viewParticipantModelList)
            }

}
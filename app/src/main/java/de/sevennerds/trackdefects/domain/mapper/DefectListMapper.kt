package de.sevennerds.trackdefects.domain.mapper

import de.sevennerds.trackdefects.data.defect_list.DefectListEntity
import de.sevennerds.trackdefects.presentation.model.StreetAddressModel
import de.sevennerds.trackdefects.presentation.model.ViewParticipantModel

object DefectListMapper {

    fun entityToModel(defectListEntity: DefectListEntity) {
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

            floorPlanEntity?.run {

            }
        }
    }

}
package de.sevennerds.trackdefects.data.defect_list.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import de.sevennerds.trackdefects.data.defect_list.DefectListEntity
import de.sevennerds.trackdefects.data.street_address.StreetAddressEntity
import de.sevennerds.trackdefects.data.view_participant.ViewParticipantEntity

class DefectListWithStreetAddress {

    @Embedded
    lateinit var defectListEntity: DefectListEntity

    @Relation(parentColumn = "id",
            entityColumn = "defect_list_id", entity = StreetAddressEntity::class)
    lateinit var streetAddressWithFloor: Set<StreetAddressWithFloor>

    @Relation(
            parentColumn = "id",
            entityColumn = "defect_list_id",
            entity = ViewParticipantEntity::class
    )
    lateinit var viewParticipantEntityList: List<ViewParticipantEntity>
}
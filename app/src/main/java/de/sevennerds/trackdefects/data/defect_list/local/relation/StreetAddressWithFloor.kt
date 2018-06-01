package de.sevennerds.trackdefects.data.defect_list.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import de.sevennerds.trackdefects.data.floor.FloorEntity
import de.sevennerds.trackdefects.data.street_address.StreetAddressEntity
import de.sevennerds.trackdefects.data.view_participant.ViewParticipantEntity

class StreetAddressWithFloor {

    @Embedded
    lateinit var streetAddressEntity: StreetAddressEntity

    @Relation(parentColumn = "id",
            entityColumn = "street_address_id", entity = FloorEntity::class)
    lateinit var floorWithLivingUnitList: List<FloorWithLivingUnit>

    @Relation(parentColumn = "id",
            entityColumn = "street_address_id", entity = ViewParticipantEntity::class)
    lateinit var viewParticipantEntityList: List<ViewParticipantEntity>
}
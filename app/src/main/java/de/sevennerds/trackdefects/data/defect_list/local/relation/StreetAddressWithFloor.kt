package de.sevennerds.trackdefects.data.defect_list.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import de.sevennerds.trackdefects.data.floor.Floor
import de.sevennerds.trackdefects.data.street_address.StreetAddress
import de.sevennerds.trackdefects.data.view_participant.ViewParticipant

class StreetAddressWithFloor {

    @Embedded
    lateinit var streetAddress: StreetAddress

    @Relation(parentColumn = "id",
            entityColumn = "street_address_id", entity = Floor::class)
    lateinit var floorWithLivingUnitList: List<FloorWithLivingUnit>

    @Relation(parentColumn = "id",
            entityColumn = "street_address_id", entity = ViewParticipant::class)
    lateinit var viewParticipantList: List<ViewParticipant>
}
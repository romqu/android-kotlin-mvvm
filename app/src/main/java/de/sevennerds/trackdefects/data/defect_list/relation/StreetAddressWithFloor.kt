package de.sevennerds.trackdefects.data.defect_list.relation

import androidx.room.Embedded
import androidx.room.Relation
import de.sevennerds.trackdefects.data.floor.Floor
import de.sevennerds.trackdefects.data.street_address.StreetAddress

class StreetAddressWithFloor {

    @Embedded
    lateinit var streetAddress: StreetAddress

    @Relation(parentColumn = "id",
            entityColumn = "street_address_id", entity = Floor::class)
    lateinit var floorList: List<FloorWithLivingUnit>
}
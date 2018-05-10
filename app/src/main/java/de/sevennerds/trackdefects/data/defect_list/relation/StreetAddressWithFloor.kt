package de.sevennerds.trackdefects.data.defect_list.relation

import androidx.room.Embedded
import androidx.room.Relation
import de.sevennerds.trackdefects.data.defect_list.Floor
import de.sevennerds.trackdefects.data.defect_list.StreetAddress

class StreetAddressWithFloor {

    @Embedded
    lateinit var streetAddress: StreetAddress

    @Relation(parentColumn = "id",
            entityColumn = "street_address_id", entity = Floor::class)
    lateinit var floorList: List<Floor>
}
package de.sevennerds.trackdefects.data.defect_list.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import de.sevennerds.trackdefects.data.defect_list.DefectList
import de.sevennerds.trackdefects.data.street_address.StreetAddress

class DefectListWithStreetAddress {

    @Embedded
    lateinit var defectList: DefectList

    @Relation(parentColumn = "id",
            entityColumn = "defect_list_id", entity = StreetAddress::class)
    lateinit var streetAddressWithFloor: Set<StreetAddressWithFloor>
}
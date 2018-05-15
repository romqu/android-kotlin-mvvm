package de.sevennerds.trackdefects.data.defect_list.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import de.sevennerds.trackdefects.data.floor.Floor
import de.sevennerds.trackdefects.data.living_unit.LivingUnit

class FloorWithLivingUnit {

    @Embedded
    lateinit var floor: Floor

    @Relation(parentColumn = "id",
            entityColumn = "floor_id", entity = LivingUnit::class)
    lateinit var livingUnitWithRoomList: List<LivingUnitWithRoom>
}
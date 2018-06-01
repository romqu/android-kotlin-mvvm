package de.sevennerds.trackdefects.data.defect_list.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import de.sevennerds.trackdefects.data.floor.FloorEntity
import de.sevennerds.trackdefects.data.living_unit.LivingUnitEntity

class FloorWithLivingUnit {

    @Embedded
    lateinit var floorEntity: FloorEntity

    @Relation(parentColumn = "id",
            entityColumn = "floor_id", entity = LivingUnitEntity::class)
    lateinit var livingUnitWithRoomList: List<LivingUnitWithRoom>
}
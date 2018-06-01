package de.sevennerds.trackdefects.data.defect_list.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import de.sevennerds.trackdefects.data.living_unit.LivingUnitEntity
import de.sevennerds.trackdefects.data.room.RoomEntity

class LivingUnitWithRoom {

    @Embedded
    lateinit var livingUnitEntity: LivingUnitEntity

    @Relation(parentColumn = "id",
            entityColumn = "living_unit_id", entity = RoomEntity::class)
    lateinit var roomWithDefectList: List<RoomWithDefect>
}
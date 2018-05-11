package de.sevennerds.trackdefects.data.defect_list.relation

import androidx.room.Embedded
import androidx.room.Relation
import de.sevennerds.trackdefects.data.living_unit.LivingUnit
import de.sevennerds.trackdefects.data.room.Room

class LivingUnitWithRoom {

    @Embedded
    lateinit var livingUnit: LivingUnit

    @Relation(parentColumn = "id",
            entityColumn = "living_unit_id", entity = Room::class)
    lateinit var roomList: List<RoomWithDefect>
}
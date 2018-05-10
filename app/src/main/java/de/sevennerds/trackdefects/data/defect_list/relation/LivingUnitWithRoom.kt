package de.sevennerds.trackdefects.data.defect_list.relation

import androidx.room.Embedded
import androidx.room.Relation
import de.sevennerds.trackdefects.data.defect_list.LivingUnit
import de.sevennerds.trackdefects.data.defect_list.Room

class LivingUnitWithRoom {

    @Embedded
    lateinit var livingUnit: LivingUnit

    @Relation(parentColumn = "id",
            entityColumn = "floor_id", entity = LivingUnit::class)
    lateinit var roomList: List<Room>
}
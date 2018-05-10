package de.sevennerds.trackdefects.data.defect_list.relation

import androidx.room.Embedded
import androidx.room.Relation
import de.sevennerds.trackdefects.data.defect_list.Floor
import de.sevennerds.trackdefects.data.defect_list.LivingUnit

class FloorWithLivingUnit {

    @Embedded
    lateinit var floor: Floor

    @Relation(parentColumn = "id",
            entityColumn = "floor_id", entity = LivingUnit::class)
    lateinit var livingUnitList: List<LivingUnit>
}
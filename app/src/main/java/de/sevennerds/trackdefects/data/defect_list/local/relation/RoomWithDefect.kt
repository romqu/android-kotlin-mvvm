package de.sevennerds.trackdefects.data.defect_list.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import de.sevennerds.trackdefects.data.defect_info.DefectInfoEntity
import de.sevennerds.trackdefects.data.room.RoomEntity

class RoomWithDefect {

    @Embedded
    lateinit var roomEntity: RoomEntity

    @Relation(parentColumn = "id",
            entityColumn = "room_id", entity = DefectInfoEntity::class)
    lateinit var defectInfoWithDefectImageList: List<DefectInfoWithDefectImage>
}
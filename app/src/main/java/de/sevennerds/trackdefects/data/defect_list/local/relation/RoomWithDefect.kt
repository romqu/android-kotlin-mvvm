package de.sevennerds.trackdefects.data.defect_list.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import de.sevennerds.trackdefects.data.defect_info.DefectInfo
import de.sevennerds.trackdefects.data.room.Room

class RoomWithDefect {

    @Embedded
    lateinit var room: Room

    @Relation(parentColumn = "id",
            entityColumn = "room_id", entity = DefectInfo::class)
    lateinit var defectInfoWithDefectImageList: List<DefectInfoWithDefectImage>
}
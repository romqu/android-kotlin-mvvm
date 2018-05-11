package de.sevennerds.trackdefects.data.defect_list.relation

import androidx.room.Embedded
import androidx.room.Relation
import de.sevennerds.trackdefects.data.defect.Defect
import de.sevennerds.trackdefects.data.room.Room

class RoomWithDefect {

    @Embedded
    lateinit var room: Room

    @Relation(parentColumn = "id",
            entityColumn = "room_id", entity = Defect::class)
    lateinit var defectList: List<DefectWithDefectImage>
}
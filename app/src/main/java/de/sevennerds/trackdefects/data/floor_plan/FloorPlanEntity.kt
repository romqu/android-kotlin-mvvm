package de.sevennerds.trackdefects.data.floor_plan

import androidx.room.Entity
import androidx.room.TypeConverters
import de.sevennerds.trackdefects.common.Constants
import de.sevennerds.trackdefects.data.PathTypeConverter

@Entity(
        tableName = "view_participant"
)
data class FloorPlanEntity constructor(
        val fileName: String,
        @TypeConverters(PathTypeConverter::class)
        val path: Path
) {
    enum class Path() {
        PHOTO_PATH(Constants.PHOTO_PATH),
        FLOORPLAN_PATH(Constants.FLOOR_PLAN_PATH)
    }
}


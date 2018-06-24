package de.sevennerds.trackdefects.data

import androidx.room.TypeConverter
import de.sevennerds.trackdefects.data.floor_plan.FloorPlanEntity

object PathTypeConverter {

    @JvmStatic
    @TypeConverter
    fun toPath(ordinal: Int): FloorPlanEntity.Path {
        return FloorPlanEntity.Path.values()[ordinal]
    }

    @JvmStatic
    @TypeConverter
    fun toOrdinal(path: FloorPlanEntity.Path): Int? {
        return path.ordinal
    }
}
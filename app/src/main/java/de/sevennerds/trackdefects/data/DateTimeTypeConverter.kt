package de.sevennerds.trackdefects.data

import androidx.room.TypeConverter
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

object DateTimeTypeConverters {

    val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @JvmStatic
    @TypeConverter
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return formatter?.parse(value, OffsetDateTime.FROM)
    }

    @JvmStatic
    @TypeConverter
    fun fromOffSetDateTime(value: OffsetDateTime?): String? {
        return value?.format(formatter)
    }

}
package de.sevennerds.trackdefects.data.defect_list

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "room", foreignKeys = [
    (ForeignKey(entity = LivingUnit::class,
            parentColumns = ["id"],
            childColumns = ["living_unit_id"],
            onDelete = ForeignKey.CASCADE))])
data class Room(@PrimaryKey(autoGenerate = true) val id: Long,
                @ColumnInfo(name = "remote_id") val remoteId: Long,
                @ColumnInfo(name = "living_unit_id") val livingUnitId: Long,
                @ColumnInfo(name = "name") val name: String,
                @ColumnInfo(name = "number") val number: Int,
                @ColumnInfo(name = "location_description") val locationDescription: String) {}
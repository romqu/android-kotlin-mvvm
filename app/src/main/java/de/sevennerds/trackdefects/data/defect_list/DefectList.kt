package de.sevennerds.trackdefects.data.defect_list

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "defect_list")
data class DefectList(@PrimaryKey(autoGenerate = true) val id: Long,
                      @ColumnInfo(name = "remote_id") val remoteId: Long,
                      @ColumnInfo(name = "name") val name: String,
                      @ColumnInfo(name = "creation_date") val creationDate: String) {
}
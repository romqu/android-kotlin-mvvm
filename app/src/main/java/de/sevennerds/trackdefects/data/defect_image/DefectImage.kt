package de.sevennerds.trackdefects.data.defect_image

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import de.sevennerds.trackdefects.data.defect.Defect

@Entity(tableName = "defect_image", foreignKeys = [
    (ForeignKey(entity = Defect::class,
            parentColumns = ["id"],
            childColumns = ["defect_id"],
            onDelete = ForeignKey.CASCADE))])
data class DefectImage(@PrimaryKey(autoGenerate = true) val id: Long,
                       @ColumnInfo(name = "remote_id") val remoteId: Long,
                       @ColumnInfo(name = "name") val name: String,
                       @ColumnInfo(name = "original_name") val originalName: String,
                       @ColumnInfo(name = "position") val position: String,
                       @ColumnInfo(name = "defect_id") val roomId: Long) {}
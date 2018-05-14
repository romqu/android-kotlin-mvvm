package de.sevennerds.trackdefects.data.defect_image

import androidx.room.*
import de.sevennerds.trackdefects.data.defect.Defect

@Entity(tableName = "defect_image", foreignKeys = [
    (ForeignKey(entity = Defect::class,
            parentColumns = ["id"],
            childColumns = ["defect_id"],
            onDelete = ForeignKey.CASCADE))],
        indices = [(Index(value = ["defect_id"], name = "defect_idx"))])
data class DefectImage(@PrimaryKey(autoGenerate = true) val id: Long,
                       @ColumnInfo(name = "remote_id") val remoteId: Long,
                       @ColumnInfo(name = "name") val name: String,
                       @ColumnInfo(name = "original_name") val originalName: String,
                       @ColumnInfo(name = "position") val position: String,
                       @ColumnInfo(name = "defect_id") val roomId: Long) {}
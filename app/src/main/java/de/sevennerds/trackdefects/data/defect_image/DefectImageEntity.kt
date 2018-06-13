package de.sevennerds.trackdefects.data.defect_image

import androidx.room.*
import de.sevennerds.trackdefects.data.defect_info.DefectInfoEntity

@Entity(
        tableName = "defect_image",
        foreignKeys = [
            ForeignKey(
                entity = DefectInfoEntity::class,
                parentColumns = ["id"],
                childColumns = ["defect_info_id"],
                onDelete = ForeignKey.CASCADE
            )
        ],
        indices = [
            Index(
                    value = "defect_info_id",
                    name = "defect_image_defect_info_idx"
            )
        ]
)
data class DefectImageEntity(
        @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Long,
        @ColumnInfo(name = "remote_id") val remoteId: Long,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "original_name") val originalName: String,
        @ColumnInfo(name = "position") val position: Int,
        @ColumnInfo(name = "defect_info_id") val defectInfoId: Long
)
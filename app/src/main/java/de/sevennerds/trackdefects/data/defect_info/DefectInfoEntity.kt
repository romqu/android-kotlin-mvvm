package de.sevennerds.trackdefects.data.defect_info

import androidx.room.*
import de.sevennerds.trackdefects.data.defect_image.DefectImageEntity
import de.sevennerds.trackdefects.data.room.RoomEntity

@Entity(
        tableName = "defect_info",
        foreignKeys = [
            ForeignKey(
                    entity = RoomEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["room_id"],
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE
            )
        ],
        indices = [
            Index(
                    value = ["room_id"],
                    name = "defect_info_room_idx"
            )
        ]
)
data class DefectInfoEntity @JvmOverloads constructor(
        @PrimaryKey(autoGenerate = true) val id: Long,
        @ColumnInfo(name = "remote_id") val remoteId: Long,
        @ColumnInfo(name = "description") val description: String,
        @ColumnInfo(name = "measure") val measure: String,
        @ColumnInfo(name = "company_in_charge") val companyInCharge: String,
        @ColumnInfo(name = "done_till") val doneTill: String,
        @ColumnInfo(name = "room_id") val roomId: Long,
        @Ignore @ColumnInfo(name = "defect_image_id") val defectImageEntityList: List<DefectImageEntity> = emptyList()
)
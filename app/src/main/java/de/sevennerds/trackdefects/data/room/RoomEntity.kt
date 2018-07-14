package de.sevennerds.trackdefects.data.room

import androidx.room.*
import de.sevennerds.trackdefects.data.defect_info.DefectInfoEntity
import de.sevennerds.trackdefects.data.living_unit.LivingUnitEntity


@Entity(
        tableName = "room",
        foreignKeys = [
            ForeignKey(
                    entity = LivingUnitEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["living_unit_id"],
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE
            )
        ],
        indices = [
            Index(
                    value = ["living_unit_id"],
                    name = "room_living_unit_idx"
            )
        ]
)
data class RoomEntity @JvmOverloads constructor(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
        @ColumnInfo(name = "remote_id") val remoteId: Long,
        @ColumnInfo(name = "living_unit_id") val livingUnitId: Long,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "number") val number: Int,
        @ColumnInfo(name = "location_description") val locationDescription: String,
        @Ignore @ColumnInfo(name = "defect_info_id") val defectInfoEntityList: List<DefectInfoEntity> = emptyList()
)
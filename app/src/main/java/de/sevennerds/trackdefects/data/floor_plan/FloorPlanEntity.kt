package de.sevennerds.trackdefects.data.floor_plan

import androidx.room.*

@Entity(
        tableName = "floor_plan",
        indices = [
            Index(
                    "file_name"
            ),
            Index(
                    "path"
            )
        ]
)
data class FloorPlanEntity(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
        @ColumnInfo(name = "file_name") val fileName: String,
        @ColumnInfo(name = "path") val path: String,
        @Ignore @ColumnInfo(name = "defect_list_id") val defectListId: Long
)
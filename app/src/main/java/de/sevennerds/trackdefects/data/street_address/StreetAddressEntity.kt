package de.sevennerds.trackdefects.data.street_address

import androidx.room.*
import de.sevennerds.trackdefects.data.defect_list.DefectListEntity
import de.sevennerds.trackdefects.data.floor.FloorEntity
import de.sevennerds.trackdefects.data.view_participant.ViewParticipantEntity


@Entity(
        tableName = "street_address",
        foreignKeys = [
            ForeignKey(
                entity = DefectListEntity::class,
                parentColumns = ["id"],
                childColumns = ["defect_list_id"],
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
            )
        ],
        indices = [
            Index(
                    value = "defect_list_id",
                    name = "street_address_defect_list_idx"
            )
        ]
)
data class StreetAddressEntity @JvmOverloads constructor(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
        @ColumnInfo(name = "remote_id") val remoteId: Long,
        @ColumnInfo(name = "defect_list_id") val defectListId: Long,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "postal_code") val postalCode: Int,
        @ColumnInfo(name = "number") val number: Int,
        @ColumnInfo(name = "additional") val additional: String,
        @ColumnInfo(name = "creation_date") val creationDate: String,
        @Ignore @ColumnInfo(name = "floor_id") val floorEntityList: List<FloorEntity> = emptyList(),
        @Ignore @ColumnInfo(name = "view_participant_id") val viewParticipantEntityList: List<ViewParticipantEntity> = emptyList()
)
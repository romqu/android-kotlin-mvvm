package de.sevennerds.trackdefects.data.defect_list

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import de.sevennerds.trackdefects.data.floor_plan.FloorPlanEntity
import de.sevennerds.trackdefects.data.street_address.StreetAddressEntity
import de.sevennerds.trackdefects.data.view_participant.ViewParticipantEntity

@Entity(tableName = "defect_list")
data class DefectListEntity @JvmOverloads constructor(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
        @ColumnInfo(name = "remote_id") val remoteId: Long,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "creation_date") val creationDate: String,
        @Ignore @ColumnInfo(name = "floor_plan_id") val floorPlanEntity: FloorPlanEntity? = null,
        @Ignore @ColumnInfo(name = "street_address_id") val streetAddressEntity: StreetAddressEntity? = null,
        @Ignore @ColumnInfo(name = "view_participant_id") val viewParticipantEntity: ViewParticipantEntity? = null
)
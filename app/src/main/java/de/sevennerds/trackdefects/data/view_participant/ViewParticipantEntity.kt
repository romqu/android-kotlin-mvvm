package de.sevennerds.trackdefects.data.view_participant

import androidx.room.*
import de.sevennerds.trackdefects.data.defect_list.DefectListEntity

@Entity(
        tableName = "view_participant",
        foreignKeys = [
            ForeignKey(
                entity = DefectListEntity::class,
                parentColumns = ["id"],
                childColumns = ["street_address_id"],
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
            )
        ],
        indices = [
            Index(
                    value = ["street_address_id"],
                    name = "view_participant_street_address_idx"
            )
        ]
)
data class ViewParticipantEntity(

        /**
         * @remoteId -- The backend primary key of the object
         * @defectListId -- FK relation with parent object DefectList
         * @streetAddressId -- FK relation with parent object StreetAddressEntity
         */

        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
        @ColumnInfo(name = "remote_id") val remoteId: Long,
        @ColumnInfo(name = "defect_list_id") val defectListId: Long,
        //@ColumnInfo(name = "street_address_id") val streetAddressId: Long, // Ignored for now because we dont have welldefined customer requirements.
        @ColumnInfo(name = "forename") val forename: String,
        @ColumnInfo(name = "surname") val surname: String,
        @ColumnInfo(name = "phone_number") val phoneNumber: Int,
        @ColumnInfo(name = "email") val email: String,
        @ColumnInfo(name = "company_name") val companyName: String
)
package de.sevennerds.trackdefects.data.view_participant

import androidx.room.*
import de.sevennerds.trackdefects.data.defect_list.DefectList

@Entity(tableName = "view_participant", foreignKeys = [
    (ForeignKey(entity = DefectList::class,
            parentColumns = ["id"],
            childColumns = ["street_address_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE))],
        indices = [(Index(value = ["street_address_id"], name = "view_participant_street_address_idx"))])
data class ViewParticipant(@PrimaryKey(autoGenerate = true) val id: Long,
                           @ColumnInfo(name = "remote_id") val remoteId: Long,
                           @ColumnInfo(name = "street_address_id") val streetAddressId: Long,
                           @ColumnInfo(name = "forename") val forename: String,
                           @ColumnInfo(name = "surname") val surname: String,
                           @ColumnInfo(name = "phone_number") val phoneNumber: Int,
                           @ColumnInfo(name = "e_mail") val email: String,
                           @ColumnInfo(name = "company_name") val companyName: String)
package de.sevennerds.trackdefects.data.defect_list

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "street_address", foreignKeys = [
    (ForeignKey(entity = DefectList::class,
            parentColumns = ["id"],
            childColumns = ["defect_list_id"],
            onDelete = ForeignKey.CASCADE))])
data class StreetAddress(@PrimaryKey(autoGenerate = true) val id: Long,
                         @ColumnInfo(name = "remote_id") val remoteId: Long,
                         @ColumnInfo(name = "defect_list_id") val defectListId: Long,
                         @ColumnInfo(name = "name") val name: String,
                         @ColumnInfo(name = "postal_code") val postalCode: Int,
                         @ColumnInfo(name = "number") val number: Int,
                         @ColumnInfo(name = "additional") val additional: String,
                         @ColumnInfo(name = "creation_date") val creationDate: String) {}
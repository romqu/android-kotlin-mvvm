package de.sevennerds.trackdefects.data.defect_list

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "floor", foreignKeys = [
    (ForeignKey(entity = StreetAddress::class,
            parentColumns = ["id"],
            childColumns = ["street_address_id"],
            onDelete = ForeignKey.CASCADE))])
data class Floor(@PrimaryKey(autoGenerate = true) val id: Long,
                 @ColumnInfo(name = "remote_id") val remoteId: Long,
                 @ColumnInfo(name = "street_address_id") val streetAddressId: Long,
                 @ColumnInfo(name = "name") val name: String) {}
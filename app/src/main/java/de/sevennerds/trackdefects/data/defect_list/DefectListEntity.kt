package de.sevennerds.trackdefects.data.defect_list

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import de.sevennerds.trackdefects.data.street_address.StreetAddressEntity

@Entity(tableName = "defect_list")
data class DefectListEntity
@JvmOverloads
constructor(@PrimaryKey(autoGenerate = true) val id: Long,
            @ColumnInfo(name = "remote_id") val remoteId: Long,
            @ColumnInfo(name = "name") val name: String,
            @ColumnInfo(name = "creation_date") val creationDate: String,
            @Ignore val streetAddressEntity: StreetAddressEntity? = null)

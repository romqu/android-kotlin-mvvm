package de.sevennerds.trackdefects.data.floor

import androidx.room.*
import de.sevennerds.trackdefects.data.living_unit.LivingUnit
import de.sevennerds.trackdefects.data.street_address.StreetAddress


@Entity(tableName = "floor", foreignKeys = [
    (ForeignKey(entity = StreetAddress::class,
            parentColumns = ["id"],
            childColumns = ["street_address_id"],
            onDelete = ForeignKey.CASCADE))])
data class Floor @JvmOverloads constructor(@PrimaryKey(autoGenerate = true) val id: Long,
                                           @ColumnInfo(name = "remote_id") val remoteId: Long,
                                           @ColumnInfo(name = "street_address_id") val streetAddressId: Long,
                                           @ColumnInfo(name = "name") val name: String,
                                           @Ignore val livingUnitList: List<LivingUnit> = emptyList()) {}
package de.sevennerds.trackdefects.data.floor

import androidx.room.*
import de.sevennerds.trackdefects.data.living_unit.LivingUnitEntity
import de.sevennerds.trackdefects.data.street_address.StreetAddressEntity


@Entity(tableName = "floorEntity", foreignKeys = [
    (ForeignKey(entity = StreetAddressEntity::class,
                parentColumns = ["id"],
                childColumns = ["street_address_id"],
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE))],
        indices = [(Index(value = ["street_address_id"], name = "floor_street_address_idx"))])
data class FloorEntity @JvmOverloads constructor(@PrimaryKey(autoGenerate = true) val id: Long,
                                                 @ColumnInfo(name = "remote_id") val remoteId: Long,
                                                 @ColumnInfo(name = "street_address_id") val streetAddressId: Long,
                                                 @ColumnInfo(name = "name") val name: String,
                                                 @Ignore val livingUnitEntityList: List<LivingUnitEntity> = emptyList())
package de.sevennerds.trackdefects.data.living_unit

import androidx.room.*
import de.sevennerds.trackdefects.data.floor.FloorEntity

@Entity(tableName = "living_unit", foreignKeys = [
    (ForeignKey(entity = FloorEntity::class,
                parentColumns = ["id"],
                childColumns = ["floor_id"],
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE))],
        indices = [(Index(value = ["floor_id"], name = "living_unit_floor_idx"))])
data class LivingUnitEntity @JvmOverloads constructor(@PrimaryKey(autoGenerate = true) val id: Long,
                                                      @ColumnInfo(name = "remote_id") val remoteId: Long,
                                                      @ColumnInfo(name = "floor_id") val floorId: Long,
                                                      @ColumnInfo(name = "number") val number: Int,
                                                      @Ignore val roomList: List<Room> = emptyList())
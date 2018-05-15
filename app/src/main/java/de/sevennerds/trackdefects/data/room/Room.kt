package de.sevennerds.trackdefects.data.room

import androidx.room.*
import de.sevennerds.trackdefects.data.defect_info.DefectInfo
import de.sevennerds.trackdefects.data.living_unit.LivingUnit


@Entity(tableName = "room", foreignKeys = [
    (ForeignKey(entity = LivingUnit::class,
            parentColumns = ["id"],
            childColumns = ["living_unit_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE))],
        indices = [(Index(value = ["living_unit_id"], name = "living_unit_idx"))])
data class Room @JvmOverloads constructor(@PrimaryKey(autoGenerate = true) val id: Long,
                                          @ColumnInfo(name = "remote_id") val remoteId: Long,
                                          @ColumnInfo(name = "living_unit_id") val livingUnitId: Long,
                                          @ColumnInfo(name = "name") val name: String,
                                          @ColumnInfo(name = "number") val number: Int,
                                          @ColumnInfo(name = "location_description") val locationDescription: String,
                                          @Ignore val defectInfoList: List<DefectInfo> = emptyList()) {}
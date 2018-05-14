package de.sevennerds.trackdefects.data.street_address

import androidx.room.*
import de.sevennerds.trackdefects.data.defect_list.DefectList
import de.sevennerds.trackdefects.data.floor.Floor


@Entity(tableName = "street_address", foreignKeys = [
    (ForeignKey(entity = DefectList::class,
            parentColumns = ["id"],
            childColumns = ["defect_list_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE))],
        indices = [(Index(value = ["defect_list_id"], name = "defect_list_idx"))])
data class StreetAddress @JvmOverloads constructor(@PrimaryKey(autoGenerate = true) val id: Long,
                                                   @ColumnInfo(name = "remote_id") val remoteId: Long,
                                                   @ColumnInfo(name = "defect_list_id") val defectListId: Long,
                                                   @ColumnInfo(name = "name") val name: String,
                                                   @ColumnInfo(name = "postal_code") val postalCode: Int,
                                                   @ColumnInfo(name = "number") val number: Int,
                                                   @ColumnInfo(name = "additional") val additional: String,
                                                   @ColumnInfo(name = "creation_date") val creationDate: String,
                                                   @Ignore val floorList: List<Floor> = emptyList()) {}
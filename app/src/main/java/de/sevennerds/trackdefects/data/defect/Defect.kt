package de.sevennerds.trackdefects.data.defect

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import de.sevennerds.trackdefects.data.room.Room

@Entity(tableName = "defect", foreignKeys = [
    (ForeignKey(entity = Room::class,
            parentColumns = ["id"],
            childColumns = ["room_id"],
            onDelete = ForeignKey.CASCADE))])
data class Defect(@PrimaryKey(autoGenerate = true) val id: Long,
                  @ColumnInfo(name = "remote_id") val remoteId: Long,
                  @ColumnInfo(name = "description") val description: String,
                  @ColumnInfo(name = "measure") val measure: String,
                  @ColumnInfo(name = "company_in_charge") val companyInCharge: String,
                  @ColumnInfo(name = "done_till") val doneTill: String,
                  @ColumnInfo(name = "room_id") val roomId: Long) {}
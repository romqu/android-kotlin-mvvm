package de.sevennerds.trackdefects.data.client

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "client")
data class Client(@PrimaryKey(autoGenerate = true) val id: Long,
                  @ColumnInfo(name = "remote_id") val remoteId: Long,
                  @ColumnInfo(name = "forename") val forename: String,
                  @ColumnInfo(name = "surname") val surname: String) {
}
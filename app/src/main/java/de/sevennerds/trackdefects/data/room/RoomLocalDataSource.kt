package de.sevennerds.trackdefects.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import de.sevennerds.trackdefects.data.BaseLocalDataSource
import de.sevennerds.trackdefects.data.LocalDataSource

@Dao
interface RoomLocalDataSource: BaseLocalDataSource<Room> {
}
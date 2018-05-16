package de.sevennerds.trackdefects.data.room

import androidx.room.Dao
import de.sevennerds.trackdefects.data.BaseLocalDataSource

@Dao
abstract class RoomLocalDataSource : BaseLocalDataSource<Room> {
}
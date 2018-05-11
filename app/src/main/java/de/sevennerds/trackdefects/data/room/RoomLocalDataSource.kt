package de.sevennerds.trackdefects.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface RoomLocalDataSource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(room: Room)
}
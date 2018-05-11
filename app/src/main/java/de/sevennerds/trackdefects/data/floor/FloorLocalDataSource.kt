package de.sevennerds.trackdefects.data.floor


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface FloorLocalDataSource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(floor: Floor)
}
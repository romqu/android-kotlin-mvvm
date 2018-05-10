package de.sevennerds.trackdefects.data.defect_list


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface FloorLocalDataSource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(floor: Floor)
}
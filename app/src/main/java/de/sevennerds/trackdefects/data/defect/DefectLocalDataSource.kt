package de.sevennerds.trackdefects.data.defect

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface DefectLocalDataSource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(defect: Defect)
}
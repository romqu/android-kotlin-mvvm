package de.sevennerds.trackdefects.data.defect_image

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface DefectImageLocalDataSource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(defectImage: DefectImage)
}
package de.sevennerds.trackdefects.data.living_unit


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface LivingUnitLocalDataSource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(livingUnit: LivingUnit)
}
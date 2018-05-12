package de.sevennerds.trackdefects.data.living_unit


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import de.sevennerds.trackdefects.data.BaseLocalDataSource

@Dao
interface LivingUnitLocalDataSource: BaseLocalDataSource<LivingUnit> {
}
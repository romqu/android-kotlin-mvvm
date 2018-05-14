package de.sevennerds.trackdefects.data

import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface BaseLocalDataSource<in T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(objList: List<T>): List<Long>
}
package de.sevennerds.trackdefects.data

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseLocalDataSource<in T> {

    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    fun insert(obj: T): Long

    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    fun insert(objList: List<T>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnore(obj: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnore(objList: List<T>): List<Long>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateIgnore(obj: T): Int

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateIgnore(objList: List<T>): Int

    // call only in transaction
    fun upsert(obj: T): Long {
        val id = insertIgnore(obj)

        if (id == -1L) {
            updateIgnore(obj)
        }

        return id
    }
}
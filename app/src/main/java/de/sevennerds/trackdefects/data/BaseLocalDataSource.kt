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

    @Update(onConflict = OnConflictStrategy.ROLLBACK)
    fun update(obj: T): Int

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateIgnore(obj: T): Int

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateIgnore(objList: List<T>): Int

    // call only in transaction
    fun upsert(obj: T, originalId: Long): Long {

        val newId = insertIgnore(obj)

        return if (newId == -1L) {
            update(obj)
            originalId
        } else {
            newId
        }
    }
}
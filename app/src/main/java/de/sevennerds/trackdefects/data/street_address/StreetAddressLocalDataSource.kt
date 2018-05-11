package de.sevennerds.trackdefects.data.street_address

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface StreetAddressLocalDataSource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(streetAddress: StreetAddress): Long
}
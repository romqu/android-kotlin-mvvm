package de.sevennerds.trackdefects.data.street_address

import androidx.room.Dao
import de.sevennerds.trackdefects.data.BaseLocalDataSource

@Dao
interface StreetAddressLocalDataSource : BaseLocalDataSource<StreetAddress> {
}
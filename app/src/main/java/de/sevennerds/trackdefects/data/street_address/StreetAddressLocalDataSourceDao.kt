package de.sevennerds.trackdefects.data.street_address

import androidx.room.Dao
import de.sevennerds.trackdefects.data.BaseLocalDataSource

@Dao
abstract class StreetAddressLocalDataSourceDao : BaseLocalDataSource<StreetAddressEntity> {
}
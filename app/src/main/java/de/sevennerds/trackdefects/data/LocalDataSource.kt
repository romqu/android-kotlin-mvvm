package de.sevennerds.trackdefects.data

import androidx.room.Database
import androidx.room.RoomDatabase
import de.sevennerds.trackdefects.data.client.Client
import de.sevennerds.trackdefects.data.client.local.ClientLocalDataSource
import de.sevennerds.trackdefects.data.defect_list.*

@Database(entities = [
    Client::class,
    DefectList::class,
    StreetAddress::class,
    Floor::class,
    LivingUnit::class],
        version = 1)
abstract class LocalDataSource : RoomDatabase() {

    abstract fun client(): ClientLocalDataSource

    abstract fun defectList(): DefectListLocalDataSource

    abstract fun streetAddress(): StreetAddressLocalDataSource

    abstract fun floor(): FloorLocalDataSource

    abstract fun livingUnit(): LivingUnitLocalDataSource
}

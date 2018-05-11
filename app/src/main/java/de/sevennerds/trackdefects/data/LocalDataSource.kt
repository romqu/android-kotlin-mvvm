package de.sevennerds.trackdefects.data

import androidx.room.Database
import androidx.room.RoomDatabase
import de.sevennerds.trackdefects.data.client.Client
import de.sevennerds.trackdefects.data.client.local.ClientLocalDataSource
import de.sevennerds.trackdefects.data.defect.Defect
import de.sevennerds.trackdefects.data.defect.DefectLocalDataSource
import de.sevennerds.trackdefects.data.defect_image.DefectImage
import de.sevennerds.trackdefects.data.defect_image.DefectImageLocalDataSource
import de.sevennerds.trackdefects.data.defect_list.DefectList
import de.sevennerds.trackdefects.data.defect_list.DefectListLocalDataSource
import de.sevennerds.trackdefects.data.floor.Floor
import de.sevennerds.trackdefects.data.floor.FloorLocalDataSource
import de.sevennerds.trackdefects.data.living_unit.LivingUnit
import de.sevennerds.trackdefects.data.living_unit.LivingUnitLocalDataSource
import de.sevennerds.trackdefects.data.room.Room
import de.sevennerds.trackdefects.data.room.RoomLocalDataSource
import de.sevennerds.trackdefects.data.street_address.StreetAddress
import de.sevennerds.trackdefects.data.street_address.StreetAddressLocalDataSource

@Database(entities = [
    Client::class,
    DefectList::class,
    StreetAddress::class,
    Floor::class,
    LivingUnit::class,
    Room::class, Defect::class,
    DefectImage::class],
        version = 1)
abstract class LocalDataSource : RoomDatabase() {

    abstract fun client(): ClientLocalDataSource

    abstract fun defectList(): DefectListLocalDataSource

    abstract fun streetAddress(): StreetAddressLocalDataSource

    abstract fun floor(): FloorLocalDataSource

    abstract fun livingUnit(): LivingUnitLocalDataSource

    abstract fun room(): RoomLocalDataSource

    abstract fun defect(): DefectLocalDataSource

    abstract fun defectImage(): DefectImageLocalDataSource


}

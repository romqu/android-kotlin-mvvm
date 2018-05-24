package de.sevennerds.trackdefects.data

import androidx.room.Database
import androidx.room.RoomDatabase
import de.sevennerds.trackdefects.data.client.ClientEntity
import de.sevennerds.trackdefects.data.client.local.ClientLocalDataSource
import de.sevennerds.trackdefects.data.defect_image.DefectImage
import de.sevennerds.trackdefects.data.defect_image.DefectImageLocalDataSource
import de.sevennerds.trackdefects.data.defect_info.DefectInfo
import de.sevennerds.trackdefects.data.defect_info.DefectInfoLocalDataSource
import de.sevennerds.trackdefects.data.defect_list.DefectList
import de.sevennerds.trackdefects.data.defect_list.local.DefectListLocalDataSource
import de.sevennerds.trackdefects.data.floor.Floor
import de.sevennerds.trackdefects.data.floor.FloorLocalDataSource
import de.sevennerds.trackdefects.data.living_unit.LivingUnit
import de.sevennerds.trackdefects.data.living_unit.LivingUnitLocalDataSource
import de.sevennerds.trackdefects.data.room.Room
import de.sevennerds.trackdefects.data.room.RoomLocalDataSource
import de.sevennerds.trackdefects.data.street_address.StreetAddress
import de.sevennerds.trackdefects.data.street_address.StreetAddressLocalDataSource
import de.sevennerds.trackdefects.data.test.TestEntity
import de.sevennerds.trackdefects.data.view_participant.ViewParticipant
import de.sevennerds.trackdefects.data.view_participant.ViewParticipantLocalDataSource

@Database(entities = [
    ClientEntity::class,
    DefectList::class,
    StreetAddress::class,
    ViewParticipant::class,
    Floor::class,
    LivingUnit::class,
    Room::class, DefectInfo::class,
    DefectImage::class, TestEntity::class],
        version = 1)
abstract class LocalDataSource : RoomDatabase() {

    abstract fun client(): ClientLocalDataSource

    abstract fun defectList(): DefectListLocalDataSource

    abstract fun streetAddress(): StreetAddressLocalDataSource

    abstract fun viewParticipant(): ViewParticipantLocalDataSource

    abstract fun floor(): FloorLocalDataSource

    abstract fun livingUnit(): LivingUnitLocalDataSource

    abstract fun room(): RoomLocalDataSource

    abstract fun defectInfo(): DefectInfoLocalDataSource

    abstract fun defectImage(): DefectImageLocalDataSource
}

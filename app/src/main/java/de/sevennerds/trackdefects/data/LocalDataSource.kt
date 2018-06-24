package de.sevennerds.trackdefects.data

import androidx.room.Database
import androidx.room.RoomDatabase
import de.sevennerds.trackdefects.common.Constants
import de.sevennerds.trackdefects.data.client.ClientEntity
import de.sevennerds.trackdefects.data.client.local.ClientLocalDataSource
import de.sevennerds.trackdefects.data.defect_image.DefectImageEntity
import de.sevennerds.trackdefects.data.defect_image.DefectImageLocalDataSource
import de.sevennerds.trackdefects.data.defect_info.DefectInfoEntity
import de.sevennerds.trackdefects.data.defect_info.DefectInfoLocalDataSource
import de.sevennerds.trackdefects.data.defect_list.DefectListEntity
import de.sevennerds.trackdefects.data.defect_list.local.DefectListLocalDataSourceDao
import de.sevennerds.trackdefects.data.floor.FloorEntity
import de.sevennerds.trackdefects.data.floor.FloorLocalDataSource
import de.sevennerds.trackdefects.data.living_unit.LivingUnitEntity
import de.sevennerds.trackdefects.data.living_unit.LivingUnitLocalDataSource
import de.sevennerds.trackdefects.data.room.RoomEntity
import de.sevennerds.trackdefects.data.room.RoomLocalDataSource
import de.sevennerds.trackdefects.data.street_address.StreetAddressEntity
import de.sevennerds.trackdefects.data.street_address.StreetAddressLocalDataSourceDao
import de.sevennerds.trackdefects.data.test.TestEntity
import de.sevennerds.trackdefects.data.view_participant.ViewParticipantEntity
import de.sevennerds.trackdefects.data.view_participant.ViewParticipantLocalDataSourceDao

@Database(
        entities = [
                ClientEntity::class,
                DefectListEntity::class,
                StreetAddressEntity::class,
                ViewParticipantEntity::class,
                FloorEntity::class,
                LivingUnitEntity::class,
                RoomEntity::class, DefectInfoEntity::class,
                DefectImageEntity::class, TestEntity::class
        ],
        version = Constants.DATABASE_VERSION
)
abstract class LocalDataSource : RoomDatabase() {

    abstract fun client(): ClientLocalDataSource
    abstract fun defectList(): DefectListLocalDataSourceDao
    abstract fun streetAddress(): StreetAddressLocalDataSourceDao
    abstract fun viewParticipant(): ViewParticipantLocalDataSourceDao
    abstract fun floor(): FloorLocalDataSource
    abstract fun livingUnit(): LivingUnitLocalDataSource
    abstract fun room(): RoomLocalDataSource
    abstract fun defectInfo(): DefectInfoLocalDataSource
    abstract fun defectImage(): DefectImageLocalDataSource

}

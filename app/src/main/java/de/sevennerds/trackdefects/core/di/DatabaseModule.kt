package de.sevennerds.trackdefects.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import de.sevennerds.trackdefects.common.Constants.Database.DATABASE_NAME
import de.sevennerds.trackdefects.data.LocalDataSource
import de.sevennerds.trackdefects.data.client.local.ClientLocalDataSource
import de.sevennerds.trackdefects.data.defect_image.DefectImageLocalDataSource
import de.sevennerds.trackdefects.data.defect_info.DefectInfoLocalDataSource
import de.sevennerds.trackdefects.data.defect_list.DefectListRepository
import de.sevennerds.trackdefects.data.defect_list.local.DefectListLocalDataSource
import de.sevennerds.trackdefects.data.floor.FloorLocalDataSource
import de.sevennerds.trackdefects.data.floor_plan.FloorPlanLocalDataSource
import de.sevennerds.trackdefects.data.floor_plan.FloorPlanRepository
import de.sevennerds.trackdefects.data.living_unit.LivingUnitLocalDataSource
import de.sevennerds.trackdefects.data.room.RoomLocalDataSource
import de.sevennerds.trackdefects.data.street_address.StreetAddressLocalDataSource
import de.sevennerds.trackdefects.data.street_address.StreetAddressRepository
import de.sevennerds.trackdefects.data.view_participant.ViewParticipantLocalDataSource
import de.sevennerds.trackdefects.data.view_participant.ViewParticipantRepository
import io.requery.android.database.sqlite.RequerySQLiteOpenHelperFactory
import javax.inject.Singleton


@Module
abstract class DatabaseModule(private val context: Context) {

    @Module
    companion object {

        @Provides
        @Singleton
        @JvmStatic
        fun provideRoom(context: Context): LocalDataSource =
                Room.databaseBuilder(context, LocalDataSource::class.java, DATABASE_NAME)
                        .openHelperFactory(RequerySQLiteOpenHelperFactory()).build()

        @Provides
        @Singleton
        @JvmStatic
        fun provideClientLocalDataSource(localDataSource: LocalDataSource): ClientLocalDataSource =
                localDataSource.client()

        @Provides
        @Singleton
        @JvmStatic
        fun provideDefectListLocalDataSource(localDataSource: LocalDataSource): DefectListLocalDataSource =
                localDataSource.defectList()

        @Provides
        @Singleton
        @JvmStatic
        fun provideStreetAddressLocalDataSource(localDataSource: LocalDataSource): StreetAddressLocalDataSource =
                localDataSource.streetAddress()

        @Provides
        @Singleton
        @JvmStatic
        fun provideViewParticipantLocalDataSource(localDataSource: LocalDataSource): ViewParticipantLocalDataSource =
                localDataSource.viewParticipant()

        @Provides
        @Singleton
        @JvmStatic
        fun provideFloorLocalDataSource(localDataSource: LocalDataSource): FloorLocalDataSource =
                localDataSource.floor()

        @Provides
        @Singleton
        @JvmStatic
        fun provideFloorPlanLocalDataSrouce(localDataSource: LocalDataSource): FloorPlanLocalDataSource = localDataSource.floorPlan()

        @Provides
        @Singleton
        @JvmStatic
        fun provideLivingUnitLocalDataSource(localDataSource: LocalDataSource): LivingUnitLocalDataSource =
                localDataSource.livingUnit()

        @Provides
        @Singleton
        @JvmStatic
        fun provideRoomLocalDataSource(localDataSource: LocalDataSource): RoomLocalDataSource =
                localDataSource.room()

        @Provides
        @Singleton
        @JvmStatic
        fun provideDefectInfoLocalDataSource(localDataSource: LocalDataSource): DefectInfoLocalDataSource =
                localDataSource.defectInfo()

        @Provides
        @Singleton
        @JvmStatic
        fun provideDefectImageLocalDataSource(localDataSource: LocalDataSource): DefectImageLocalDataSource =
                localDataSource.defectImage()

        @Provides
        @Singleton
        @JvmStatic
        fun provideDefectListRepository(
                defectListLocalDataSource: DefectListLocalDataSource,
                localDataSource: LocalDataSource,
                floorPlanRepository: FloorPlanRepository,
                streetAddressRepository: StreetAddressRepository,
                viewParticipantRepository: ViewParticipantRepository
        ): DefectListRepository =
                DefectListRepository(
                        defectListLocalDataSource,
                        localDataSource,
                        streetAddressRepository,
                        viewParticipantRepository,
                        floorPlanRepository
                )

        @Provides
        @Singleton
        @JvmStatic
        fun provideFloorPlanRepository(floorPlanLocalDataSource: FloorPlanLocalDataSource): FloorPlanRepository =
                FloorPlanRepository(floorPlanLocalDataSource)

        @Provides
        @Singleton
        @JvmStatic
        fun provideViewParticipantRepository(viewParticipantLocalDataSource: ViewParticipantLocalDataSource): ViewParticipantRepository =
                ViewParticipantRepository(viewParticipantLocalDataSource)

        @Provides
        @Singleton
        @JvmStatic
        fun provideStreetAddressRepository(streetAddressLocalDataSource: StreetAddressLocalDataSource): StreetAddressRepository =
                StreetAddressRepository(streetAddressLocalDataSource)

    }

}
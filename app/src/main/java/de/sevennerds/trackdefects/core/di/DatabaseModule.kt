package de.sevennerds.trackdefects.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import de.sevennerds.trackdefects.common.DATABASE_NAME
import de.sevennerds.trackdefects.data.LocalDataSource
import de.sevennerds.trackdefects.data.client.local.ClientLocalDataSource
import de.sevennerds.trackdefects.data.defect_image.DefectImageLocalDataSource
import de.sevennerds.trackdefects.data.defect_info.DefectInfoLocalDataSource
import de.sevennerds.trackdefects.data.defect_list.local.DefectListLocalDataSource
import de.sevennerds.trackdefects.data.floor.FloorLocalDataSource
import de.sevennerds.trackdefects.data.living_unit.LivingUnitLocalDataSource
import de.sevennerds.trackdefects.data.room.RoomLocalDataSource
import de.sevennerds.trackdefects.data.street_address.StreetAddressLocalDataSource
import de.sevennerds.trackdefects.data.view_participant.ViewParticipantLocalDataSource
import io.requery.android.database.sqlite.RequerySQLiteOpenHelperFactory
import javax.inject.Singleton


@Module
abstract class DatabaseModule {

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

    }

}
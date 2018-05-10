package de.sevennerds.trackdefects.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import de.sevennerds.trackdefects.data.LocalDataSource
import de.sevennerds.trackdefects.data.client.local.ClientLocalDataSource
import de.sevennerds.trackdefects.data.defect_list.DefectListLocalDataSource
import de.sevennerds.trackdefects.data.defect_list.FloorLocalDataSource
import de.sevennerds.trackdefects.data.defect_list.LivingUnitLocalDataSource
import de.sevennerds.trackdefects.data.defect_list.StreetAddressLocalDataSource
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
                Room.databaseBuilder(context, LocalDataSource::class.java, "trackdefects.db")
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
        fun provideFloorLocalDataSource(localDataSource: LocalDataSource): FloorLocalDataSource =
                localDataSource.floor()

        @Provides
        @Singleton
        @JvmStatic
        fun provideLivingUnitLocalDataSource(localDataSource: LocalDataSource): LivingUnitLocalDataSource =
                localDataSource.livingUnit()

    }

}
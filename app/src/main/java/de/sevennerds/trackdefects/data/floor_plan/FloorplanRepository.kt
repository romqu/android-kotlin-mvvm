package de.sevennerds.trackdefects.data.floor_plan

import de.sevennerds.trackdefects.data.LocalDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FloorplanRepository @Inject constructor(
        private val floorPlanLocalDataSourceDao: FloorPlanLocalDataSourceDao,
        private val localDataSource: LocalDataSource
) {

}
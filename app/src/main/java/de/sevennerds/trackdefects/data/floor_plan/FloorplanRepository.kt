package de.sevennerds.trackdefects.data.floor_plan

import de.sevennerds.trackdefects.data.LocalDataSource
import de.sevennerds.trackdefects.data.response.Result
import io.reactivex.Single
import java.util.concurrent.Callable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FloorplanRepository @Inject constructor(
        private val floorPlanLocalDataSource: FloorPlanLocalDataSource,
        private val localDataSource: LocalDataSource
) {
    fun insert(floorPlanEntity: FloorPlanEntity): Single<Result<Long>> {
        return Single.fromCallable {
            localDataSource.runInTransaction (
                    Callable {
                        val floorPlanEntityId = floorPlanLocalDataSource.insert(floorPlanEntity)
                        Result.Success(floorPlanEntityId)
                    }
            )
        }
    }
}
package de.sevennerds.trackdefects.data.floor_plan

import com.orhanobut.logger.Logger
import de.sevennerds.trackdefects.common.Constants.Database.DATABASE_TRANSACTION_FAILED
import de.sevennerds.trackdefects.common.asSingle
import de.sevennerds.trackdefects.data.response.Error
import de.sevennerds.trackdefects.data.response.Result
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FloorPlanRepository @Inject constructor(
        private val floorPlanLocalDataSource: FloorPlanLocalDataSource
) {
    fun insert(floorPlanEntity: FloorPlanEntity): Single<Result<Long>> =
            floorPlanEntity
                    .asSingle()
                    .doOnError {
                        Logger.d("Inserting: $it")
                    }
                    .map { it ->
                        val floorPlanEntityId = floorPlanLocalDataSource.insert(it)
                        Result.success(floorPlanEntityId)
                    }
                    .doOnError {
                        Logger.d("Error: $it")
                    }
                    .onErrorReturn {
                        Result.failure(Error.DatabaseError(DATABASE_TRANSACTION_FAILED))
                    }

}
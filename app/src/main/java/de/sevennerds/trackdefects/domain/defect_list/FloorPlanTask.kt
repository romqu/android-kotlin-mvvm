package de.sevennerds.trackdefects.domain.defect_list

import com.orhanobut.logger.Logger
import de.sevennerds.trackdefects.common.Constants.Database.DATABASE_TRANSACTION_FAILED
import de.sevennerds.trackdefects.common.Constants.Database.DATABASE_TRANSACTION_SUCCEEDED
import de.sevennerds.trackdefects.data.floor_plan.FloorPlanEntity
import de.sevennerds.trackdefects.data.floor_plan.FloorplanRepository
import de.sevennerds.trackdefects.data.response.Error
import de.sevennerds.trackdefects.data.response.Result
import io.reactivex.Observable
import javax.inject.Inject

class FloorPlanTask @Inject constructor(
        private val floorplanRepository: FloorplanRepository
) {
    fun insertFloorPlanEntity(floorPlanEntity: FloorPlanEntity): Observable<Result<String>> {
        return Observable.just(floorPlanEntity).doOnNext {
            Logger.d("Saving $it")
        }.flatMapSingle { it ->
            floorplanRepository.insert(it)
        }.toList()
                .toObservable()
                .map { it ->
                    if (it.contains(Result.failure(Error.DatabaseError(DATABASE_TRANSACTION_FAILED)))) {
                        Result.failure(Error.DatabaseError(DATABASE_TRANSACTION_FAILED))
                    } else {
                        Result.success(DATABASE_TRANSACTION_SUCCEEDED)
                    }
                }.doOnError {
                    Logger.d(DATABASE_TRANSACTION_FAILED)
                }.onErrorReturn {
                    Result.failure(Error.DatabaseError(DATABASE_TRANSACTION_FAILED))
                }
    }

    fun insertFloorPlanEntityAll(floorPlanEntityList: List<FloorPlanEntity>): Observable<Result<String>> {
        return Observable.fromIterable(floorPlanEntityList)
                .doOnNext {
                    Logger.d("Saving $it")
                }.flatMap { it ->
                    insertFloorPlanEntity(it)
                }.toList()
                .toObservable()
                .map {
                    if (it.contains(Result.failure(Error.DatabaseError(DATABASE_TRANSACTION_FAILED)))) {
                        Result.failure(Error.DatabaseError(DATABASE_TRANSACTION_FAILED))
                    } else {
                        Result.success(DATABASE_TRANSACTION_SUCCEEDED)
                    }
                }.doOnError {
                    Logger.d(DATABASE_TRANSACTION_FAILED)
                }.onErrorReturn { it ->
                    Result.failure(Error.DatabaseError(DATABASE_TRANSACTION_FAILED))
                }
    }
}
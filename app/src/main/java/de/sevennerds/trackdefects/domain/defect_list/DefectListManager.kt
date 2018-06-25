package de.sevennerds.trackdefects.domain.defect_list

import com.orhanobut.logger.Logger
import de.sevennerds.trackdefects.common.Constants
import de.sevennerds.trackdefects.data.defect_list.DefectListEntity
import de.sevennerds.trackdefects.data.response.Error
import de.sevennerds.trackdefects.data.response.Result
import io.reactivex.Observable
import javax.inject.Inject

class DefectListManager @Inject constructor(
        private val defectListTask: DefectListTask,
        private val floorPlanTask: FloorPlanTask,
        private val viewParticipantTask: ViewParticipantTask,
        private val streetAddressTask: StreetAddressTask
) {

    fun insert(defectListEntity: DefectListEntity): Observable<Result<String>> {

        /**
         *      Insert DefectList with related entities
         *
         *          - InsertDefectListEntity (PARENT)
         *          - InsertStreetAddressEntity (CHILD)
         *          - InsertFloorPlanEntity (CHILD)
         *          - InsetViewParticipant (CHILD)
         */

        return Observable.just(defectListEntity).doOnNext {
            Logger.d("Saving $it")
        }.map {
            it -> defectListTask.insertDefectList(it)
            floorPlanTask.insertFloorPlanEntity(it.floorPlanEntity)
            streetAddressTask.insertStreetAddressEntity(it.streetAddressEntity)
            viewParticipantTask.insertViewParticipantEntity(it.viewParticipantEntity)
            Result.success(Constants.DATABASE_TRANSACTION_SUCCEEDED)
        }.doOnError {
            Logger.d("oh shit")
        }.onErrorReturn {
            Result.failure(Error.DatabaseError(Constants.DATABASE_TRANSACTION_FAILED))
        }
    }

    fun insertAll(defectListEntityList: List<DefectListEntity>): Observable<Result<String>> {

        /**
         *      Convenience method to insert List of DefectList with related entities.
         */

        return Observable.fromIterable(defectListEntityList).doOnNext {
            Logger.d("List is $it")
        }.flatMap {
            it -> insert(it)
        }.toList().toObservable().map{
            if (it.contains(Result.failure(Error.DatabaseError(Constants.DATABASE_TRANSACTION_FAILED)))) {
                Result.failure(Error.DatabaseError(Constants.DATABASE_TRANSACTION_FAILED))
            } else {
                Result.success(Constants.DATABASE_TRANSACTION_SUCCEEDED)
            }
        }.doOnError {
            Logger.d(Constants.DATABASE_TRANSACTION_FAILED)
        }.onErrorReturn {
            Result.failure(Error.DatabaseError(Constants.DATABASE_TRANSACTION_FAILED))
        }
    }

}
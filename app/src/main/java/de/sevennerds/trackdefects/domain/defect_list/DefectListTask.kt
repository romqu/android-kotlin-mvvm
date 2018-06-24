package de.sevennerds.trackdefects.domain.defect_list

import com.orhanobut.logger.Logger
import de.sevennerds.trackdefects.common.Constants
import de.sevennerds.trackdefects.common.Constants.Database.DATABASE_TRANSACTION_FAILED
import de.sevennerds.trackdefects.data.defect_list.DefectListEntity
import de.sevennerds.trackdefects.data.defect_list.DefectListRepository
import de.sevennerds.trackdefects.data.response.Error
import de.sevennerds.trackdefects.data.response.Result
import de.sevennerds.trackdefects.data.street_address.StreetAddressRepository
import de.sevennerds.trackdefects.data.view_participant.ViewParticipantRepository
import io.reactivex.Observable
import javax.inject.Inject


class DefectListTask @Inject constructor(
        private val defectListRepository: DefectListRepository,
        private val streetAddressRepository: StreetAddressRepository,
        private val viewParticipantRepository: ViewParticipantRepository
) {

    fun insert() {
        /**
         *      Insert DefectList with related entities
         *
         *          - InsertDefectListEntity
         *          - InsertStreetAddressEntity
         *          - InsertFloorPlanEntity
         */
    }

    fun insertAll() {
        /**
         *      Convenience method to insert List of DefectList with related entities.
         */
    }

    fun insertDefectList(defectListEntity: DefectListEntity): Observable<Result<String>> {

        /**
         *      Dont be fooled by the name, its not actually a LIST.
         *      > git blame romqu
         */

        return Observable.just(defectListEntity).doOnNext {
            Logger.d("Saving $it")
        }.flatMap {
            it -> defectListRepository.insert()
        }.toList().toObservable().map {
            it -> if (it.contains(Result.failure(Error.DatabaseError(Constants.DATABASE_TRANSACTION_FAILED)))) {
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

    fun insertDefectListAll(defectListEntityList: List<DefectListEntity>): Observable<Result<String>> {
        return Observable.fromIterable(defectListEntityList)
                .doOnNext {
                    Logger.d("Saving $it")
                }.flatMap {
                    it -> insertDefectList(it)
                }.toList().toObservable().map {
                    if (it.contains(Result.failure(Error.DatabaseError(DATABASE_TRANSACTION_FAILED)))) {
                        Result.failure(Error.DatabaseError(DATABASE_TRANSACTION_FAILED))
                    } else {
                        Result.success(Constants.DATABASE_TRANSACTION_SUCCEEDED)
                    }
                }.doOnError {
                    Logger.d(Constants.DATABASE_TRANSACTION_FAILED)
                }.onErrorReturn {
                    it -> Result.failure(Error.DatabaseError(DATABASE_TRANSACTION_FAILED))
                }
    }


    fun insertStreetAddressEntity() {

    }

    fun insertStreetAddressEntityAll() {

    }


    fun insertViewParticipantEntity() {

    }

    fun insertViewParticipantEntityAll() {

    }

    fun insertFloorPlanEntity() {

    }

    fun insertFloorPlanEntityAll() {

    }
}
package de.sevennerds.trackdefects.domain.defect_list

import com.orhanobut.logger.Logger
import de.sevennerds.trackdefects.common.Constants.Database.DATABASE_TRANSACTION_FAILED
import de.sevennerds.trackdefects.common.Constants.Database.DATABASE_TRANSACTION_SUCCEEDED
import de.sevennerds.trackdefects.data.defect_list.DefectListEntity
import de.sevennerds.trackdefects.data.defect_list.DefectListRepository
import de.sevennerds.trackdefects.data.response.Error
import de.sevennerds.trackdefects.data.response.Result
import io.reactivex.Observable
import javax.inject.Inject


class DefectListTask @Inject constructor(
        private val defectListRepository: DefectListRepository
) {

    fun insertDefectList(defectListEntity: DefectListEntity): Observable<Result<String>> {

        /**
         *      Dont be fooled by the name, its not actually a LIST.
         *      > git blame romqu
         */

        return Observable.just(defectListEntity).doOnNext {
            Logger.d("Saving $it")
        }.flatMapSingle {
            it -> defectListRepository.insert(it)
        }.toList().toObservable().map {
            it -> if (it.contains(Result.failure(Error.DatabaseError(DATABASE_TRANSACTION_FAILED)))) {
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
                        Result.success(DATABASE_TRANSACTION_SUCCEEDED)
                    }
                }.doOnError {
                    Logger.d(DATABASE_TRANSACTION_FAILED)
                }.onErrorReturn {
                    it -> Result.failure(Error.DatabaseError(DATABASE_TRANSACTION_FAILED))
                }
    }
}
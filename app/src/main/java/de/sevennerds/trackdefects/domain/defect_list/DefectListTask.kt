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

    fun insert(defectListEntity: DefectListEntity): Observable<Result<DefectListEntity>> =

            /**
             *      Dont be fooled by the name, its not actually a LIST.
             *      > git blame romqu
             */

            Observable.just(defectListEntity)
                    .doOnNext {
                        Logger.d("Saving $it")
                    }.map { it ->
                        defectListRepository.insert(it)
                        Result.success(it)
                    }.doOnError {
                        Logger.d(DATABASE_TRANSACTION_FAILED)
                    }.onErrorReturn {
                        Result.failure(Error.DatabaseError(DATABASE_TRANSACTION_FAILED))
                    }

    @Suppress("UNCHECKED_CAST")
    fun insertAll(defectListEntityList: List<DefectListEntity>): Observable<Result<DefectListEntity>> =
            Observable.fromIterable(defectListEntityList)
                    .doOnNext {
                        Logger.d("Saving $it")
                    }.flatMap { it ->
                        insert(it)
                    }.toList()
                    .toObservable()
                    .map { it ->
                        Result.success(it) as Result<DefectListEntity>
                    }.doOnError {
                        Logger.d(DATABASE_TRANSACTION_FAILED)
                    }.onErrorReturn {
                        Result.failure(Error.DatabaseError(DATABASE_TRANSACTION_FAILED))
                    }

}
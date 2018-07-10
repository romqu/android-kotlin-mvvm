package de.sevennerds.trackdefects.domain

import de.sevennerds.trackdefects.common.asObservable
import de.sevennerds.trackdefects.data.defect_list.DefectListEntity
import de.sevennerds.trackdefects.data.defect_list.DefectListRepository
import de.sevennerds.trackdefects.data.response.Result
import io.reactivex.Observable
import javax.inject.Inject


class DefectListTask @Inject constructor(
        private val defectListRepository: DefectListRepository
) {

    fun insert(request: Request<DefectListEntity>): Observable<Result<DefectListEntity>> =
            request.asObservable()
                    .flatMapSingle { it ->
                        defectListRepository.insert(it.data)
                    }

    /*@Suppress("UNCHECKED_CAST")
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
                    }*/

}
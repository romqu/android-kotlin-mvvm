package de.sevennerds.trackdefects.domain.defect_list

import com.orhanobut.logger.Logger
import de.sevennerds.trackdefects.common.Constants.Database.DATABASE_TRANSACTION_FAILED
import de.sevennerds.trackdefects.common.Constants.Database.DATABASE_TRANSACTION_SUCCEEDED
import de.sevennerds.trackdefects.data.response.Error
import de.sevennerds.trackdefects.data.response.Result
import de.sevennerds.trackdefects.data.street_address.StreetAddressEntity
import de.sevennerds.trackdefects.data.street_address.StreetAddressRepository
import io.reactivex.Observable
import javax.inject.Inject

class StreetAddressTask @Inject constructor(
        private val streetAddressRepository: StreetAddressRepository
) {
    fun insertStreetAddressEntity(streetAddressEntity: StreetAddressEntity): Observable<Result<String>> {
        return Observable.just(streetAddressEntity).doOnNext {
            Logger.d("Saving $it")
        }.flatMapSingle { it ->
            streetAddressRepository.insert(it)
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

    fun insertStreetAddressEntityAll(streetAddressEntityList: List<StreetAddressEntity>): Observable<Result<String>> {
        return Observable.fromIterable(streetAddressEntityList)
                .doOnNext {
                    Logger.d("Saving $it")
                }.flatMap { it ->
                    insertStreetAddressEntity(it)
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
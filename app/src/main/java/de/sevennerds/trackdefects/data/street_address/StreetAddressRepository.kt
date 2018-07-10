package de.sevennerds.trackdefects.data.street_address

import com.orhanobut.logger.Logger
import de.sevennerds.trackdefects.common.Constants.Database.DATABASE_TRANSACTION_FAILED
import de.sevennerds.trackdefects.data.response.Error
import de.sevennerds.trackdefects.data.response.Result
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StreetAddressRepository @Inject constructor(
        private val streetAddressLocalDataSource: StreetAddressLocalDataSource
) {
    fun insert(streetAddressEntity: StreetAddressEntity): Single<Result<Long>> =
            Single.just(streetAddressEntity)
                    .doOnError {
                        Logger.d("Inserting: $it")
                    }.map { it ->
                        val streetAddressEntityId = streetAddressLocalDataSource.insert(it)
                        Result.success(streetAddressEntityId)
                    }.doOnError {
                        Logger.d("Error: $it")
                    }.onErrorReturn {
                        Result.failure(Error.DatabaseError(DATABASE_TRANSACTION_FAILED))
                    }
}
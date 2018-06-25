package de.sevennerds.trackdefects.data.street_address

import de.sevennerds.trackdefects.data.LocalDataSource
import de.sevennerds.trackdefects.data.response.Result
import io.reactivex.Single
import java.util.concurrent.Callable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StreetAddressRepository @Inject constructor(
        private val streetAddressLocalDataSource: StreetAddressLocalDataSource,
        private val localDataSource: LocalDataSource
) {
    fun insert(streetAddressEntity: StreetAddressEntity): Single<Result<Long>> {
        return Single.fromCallable {
            localDataSource.runInTransaction (
                    Callable {
                        val streetAddressEntityId = streetAddressLocalDataSource.insert(streetAddressEntity)
                        Result.Success(streetAddressEntityId)
                    }
            )
        }
    }
}
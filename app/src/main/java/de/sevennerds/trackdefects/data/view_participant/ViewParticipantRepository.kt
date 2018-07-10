package de.sevennerds.trackdefects.data.view_participant

import com.orhanobut.logger.Logger
import de.sevennerds.trackdefects.common.Constants.Database.DATABASE_TRANSACTION_FAILED
import de.sevennerds.trackdefects.data.response.Error
import de.sevennerds.trackdefects.data.response.Result
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewParticipantRepository @Inject constructor(
        private val viewParticipantLocalDataSource: ViewParticipantLocalDataSource
) {
    fun insert(viewParticipantEntity: ViewParticipantEntity): Single<Result<Long>> =
            Single.just(viewParticipantEntity)
                    .doOnError {
                        Logger.d("Inserting: $it")
                    }.map { it ->
                        val streetAddressEntityId = viewParticipantLocalDataSource.insert(it)
                        Result.success(streetAddressEntityId)
                    }.doOnError {
                        Logger.d("Error: $it")
                    }.onErrorReturn {
                        Result.failure(Error.DatabaseError(DATABASE_TRANSACTION_FAILED))
                    }
}
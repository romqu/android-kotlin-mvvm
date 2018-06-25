package de.sevennerds.trackdefects.domain.defect_list

import com.orhanobut.logger.Logger
import de.sevennerds.trackdefects.common.Constants.Database.DATABASE_TRANSACTION_FAILED
import de.sevennerds.trackdefects.common.Constants.Database.DATABASE_TRANSACTION_SUCCEEDED
import de.sevennerds.trackdefects.data.response.Error
import de.sevennerds.trackdefects.data.response.Result
import de.sevennerds.trackdefects.data.view_participant.ViewParticipantEntity
import de.sevennerds.trackdefects.data.view_participant.ViewParticipantRepository
import io.reactivex.Observable
import javax.inject.Inject

class ViewParticipantTask @Inject constructor(
        private val viewParticipantRepository: ViewParticipantRepository
) {
    fun insertViewParticipantEntity(viewParticipantEntity: ViewParticipantEntity): Observable<Result<String>> {
        return Observable.just(viewParticipantEntity).doOnNext {
            Logger.d("Saving $it")
        }.flatMapSingle {
            it -> viewParticipantRepository.insert(it)
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

    fun insertViewParticipantEntityAll(viewParticipantEntityList: List<ViewParticipantEntity>): Observable<Result<String>> {
        return Observable.fromIterable(viewParticipantEntityList)
                .doOnNext {
                    Logger.d("Saving $it")
                }.flatMap {
                    it -> insertViewParticipantEntity(it)
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
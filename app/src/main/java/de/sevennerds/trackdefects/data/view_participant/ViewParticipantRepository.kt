package de.sevennerds.trackdefects.data.view_participant

import de.sevennerds.trackdefects.data.LocalDataSource
import de.sevennerds.trackdefects.data.response.Result
import io.reactivex.Single
import java.util.concurrent.Callable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewParticipantRepository @Inject constructor(
        private val viewParticipantLocalDataSourceDao: ViewParticipantLocalDataSourceDao,
        private val localDataSource: LocalDataSource
) {
    fun insert(viewParticipantEntity: ViewParticipantEntity): Single<Result<Long>> {
        return Single.fromCallable {
            localDataSource.runInTransaction (
                    Callable {
                        val viewParticipantEntityId = viewParticipantLocalDataSourceDao.insert(viewParticipantEntity)
                        Result.Success(viewParticipantEntityId)
                    }
            )
        }
    }
}
package de.sevennerds.trackdefects.data.defect_list

import com.orhanobut.logger.Logger
import de.sevennerds.trackdefects.common.Constants.Database.DATABASE_TRANSACTION_FAILED
import de.sevennerds.trackdefects.data.LocalDataSource
import de.sevennerds.trackdefects.data.defect_list.local.DefectListLocalDataSource
import de.sevennerds.trackdefects.data.floor_plan.FloorPlanRepository
import de.sevennerds.trackdefects.data.response.Error
import de.sevennerds.trackdefects.data.response.Result
import de.sevennerds.trackdefects.data.street_address.StreetAddressRepository
import de.sevennerds.trackdefects.data.view_participant.ViewParticipantRepository
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.Callable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefectListRepository @Inject constructor(
        private val defectListLocalDataSource: DefectListLocalDataSource,
        private val localDataSource: LocalDataSource,
        private val streetAddressRepository: StreetAddressRepository,
        private val viewParticipantRepository: ViewParticipantRepository,
        private val floorPlanRepository: FloorPlanRepository
) {

    fun insert(defectListEntity: DefectListEntity): Single<Result<DefectListEntity>> =
            Single.fromCallable {

                val streetAddressEntity = defectListEntity.streetAddressEntity
                val viewParticipantEntity = defectListEntity.viewParticipantEntity
                val floorPlanEntity = defectListEntity.floorPlanEntity

                localDataSource.runInTransaction(Callable {

                    val defectListId = defectListLocalDataSource.insert(defectListEntity)
                    val newDefectListEntity = defectListEntity.copy(
                            id = defectListId
                    )

                    val newStreetAddressEntity = streetAddressEntity.copy(
                            defectListId = defectListId
                    )
                    streetAddressRepository.insert(newStreetAddressEntity)

                    val newViewParticipantEntity = viewParticipantEntity.copy(
                            defectListId = defectListId
                    )

                    viewParticipantRepository.insert(newViewParticipantEntity)

                    val newFloorPlanEntity = floorPlanEntity.copy(
                            defectListId = defectListId
                    )
                    floorPlanRepository.insert(newFloorPlanEntity)
                    Result.Success(newDefectListEntity)
                })
            }

    fun insertAll(defectListEntityList: List<DefectListEntity>): Observable<Result<DefectListEntity>> =
            Observable.fromIterable(defectListEntityList)
                    .doOnNext {
                        Logger.d("$it")
                    }.flatMapSingle { it ->
                        insert(it)
                    }.doOnError {
                        Logger.d(DATABASE_TRANSACTION_FAILED)
                    }.onErrorReturn {
                        Result.failure(Error.DatabaseError(DATABASE_TRANSACTION_FAILED))
                    }

}

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

                localDataSource.runInTransaction(Callable {

                    val defectListId = defectListLocalDataSource.insert(defectListEntity)

                    val streetAddressEntity = defectListEntity.streetAddressEntity!!.copy(
                            defectListId = defectListId
                    )
                    val streetAddressEntityId = streetAddressRepository.insert(streetAddressEntity)


                    val viewParticipantEntity = defectListEntity.viewParticipantEntity!!.copy(
                            defectListId = defectListId
                    )

                    val viewParticipantEntityId = viewParticipantRepository.insert(viewParticipantEntity)

                    
                    val id: Long = when (viewParticipantEntityId) {
                        is Result.Success<Long> -> viewParticipantEntityId.data
                        //is Result.Failure<> -> 0L
                        else -> 0L
                    }

                    val floorPlanEntity = defectListEntity.floorPlanEntity!!.copy(
                            defectListId = defectListId
                    )

                    val floorPlanEntityId = floorPlanRepository.insert(floorPlanEntity)


                    val newStreetAddressEntity = streetAddressEntity.copy(
                            id = streetAddressEntityId
                    )

                    val newViewParticipantEntity = viewParticipantEntity.copy(
                            id = viewParticipantEntityId
                    )

                    val newFloorPlanEntity = floorPlanEntity.copy(
                            id = floorPlanEntityId
                    )

                    // we add the newfloorplan shit and view here aswell then we win
                    val newDefectListEntity = defectListEntity.copy(
                            id = defectListId,
                            floorPlanEntity = newFloorPlanEntity,
                            viewParticipantEntity = newViewParticipantEntity,
                            streetAddressEntity = newStreetAddressEntity
                    )
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

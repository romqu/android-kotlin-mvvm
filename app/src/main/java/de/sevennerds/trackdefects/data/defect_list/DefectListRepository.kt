package de.sevennerds.trackdefects.data.defect_list

import de.sevennerds.trackdefects.common.asObservable
import de.sevennerds.trackdefects.data.LocalDataSource
import de.sevennerds.trackdefects.data.defect_list.local.DefectListLocalDataSource
import de.sevennerds.trackdefects.data.floor_plan.FloorPlanRepository
import de.sevennerds.trackdefects.data.response.Result
import de.sevennerds.trackdefects.data.street_address.StreetAddressRepository
import de.sevennerds.trackdefects.data.view_participant.ViewParticipantRepository
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
            localDataSource.runInTransaction(Callable {

                defectListLocalDataSource.insert(defectListEntity)
                        .asObservable()
                        .map { defectListEntityId ->
                            defectListEntity.copy(id = defectListEntityId)
                        }
                        .map { defectListEntity ->
                            defectListEntity
                                    .copy(streetAddressEntity = defectListEntity.streetAddressEntity!!
                                            .copy(
                                                    defectListId = defectListEntity.id
                                            ))
                        }
                        .flatMap { defectListEntity ->

                            val streetAddressEntity = defectListEntity.streetAddressEntity!!

                            streetAddressRepository
                                    .insert(streetAddressEntity)
                                    .map { result: Result<Long> ->
                                        result.getOrThrow()
                                    }
                                    .map { streetAddressEntityId ->
                                        defectListEntity.copy(streetAddressEntity = streetAddressEntity
                                                .copy(id = streetAddressEntityId))
                                    }
                                    .toObservable()
                        }
                        .map { defectListEntity ->
                            defectListEntity.copy(viewParticipantEntity = defectListEntity
                                    .viewParticipantEntity!!
                                    .copy(defectListId = defectListEntity.id))
                        }.flatMap { defectListEntity ->

                            val viewParticipantEntity = defectListEntity.viewParticipantEntity!!

                            viewParticipantRepository
                                    .insert(viewParticipantEntity)
                                    .map { result: Result<Long> ->
                                        result.getOrThrow()
                                    }
                                    .map { viewParticipantEnityId: Long ->
                                        defectListEntity.copy(viewParticipantEntity = viewParticipantEntity
                                                .copy(id = viewParticipantEnityId))
                                    }.toObservable()
                        }.map { defectListEntity ->
                            Result.success(defectListEntity)
                        }.singleOrError()
            })


    /*fun insertAll(defectListEntityList: List<DefectListEntity>): Observable<Result<DefectListEntity>> =
            Observable.fromIterable(defectListEntityList)
                    .doOnNext {
                        Logger.d("$it")
                    }.flatMapSingle { it ->
                        insert(it)
                    }.doOnError {
                        Logger.d(DATABASE_TRANSACTION_FAILED)
                    }.onErrorReturn {
                        Result.failure(Error.DatabaseError(DATABASE_TRANSACTION_FAILED))
                    }*/

}

package de.sevennerds.trackdefects.data.defect_list

import com.orhanobut.logger.Logger
import de.sevennerds.trackdefects.common.asObservable
import de.sevennerds.trackdefects.common.asSingle
import de.sevennerds.trackdefects.data.LocalDataSource
import de.sevennerds.trackdefects.data.defect_list.local.DefectListLocalDataSource
import de.sevennerds.trackdefects.data.floor_plan.FloorPlanRepository
import de.sevennerds.trackdefects.data.response.Error
import de.sevennerds.trackdefects.data.response.Result
import de.sevennerds.trackdefects.data.street_address.StreetAddressRepository
import de.sevennerds.trackdefects.data.view_participant.ViewParticipantRepository
import io.reactivex.Flowable
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

            defectListEntity
                    .asSingle()
                    .flatMap {

                        localDataSource.runInTransaction(Callable {

                            defectListLocalDataSource
                                    .insert(defectListEntity)
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
                                        defectListEntity.copy(viewParticipantEntityList = defectListEntity
                                                .viewParticipantEntityList
                                                .map { it.copy(defectListId = defectListEntity.id) })
                                    }
                                    .flatMap { defectListEntity ->

                                        val viewParticipantEntityList = defectListEntity.viewParticipantEntityList

                                        viewParticipantRepository
                                                .insert(viewParticipantEntityList)
                                                .map { result: Result<List<Long>> ->
                                                    result.getOrThrow()
                                                }
                                                .map { viewParticipantEnityIdList: List<Long> ->

                                                    val newViewParticipantEntityList = viewParticipantEntityList
                                                            .mapIndexed { index,
                                                                          viewParticipantEntity ->
                                                                viewParticipantEntity.copy(id = viewParticipantEnityIdList[index])
                                                            }


                                                    defectListEntity.copy(viewParticipantEntityList = newViewParticipantEntityList)
                                                }
                                                .toObservable()
                                    }
                                    .flatMapSingle { defectListEntity ->

                                        val floorPlanEntity = defectListEntity.floorPlanEntity

                                        floorPlanRepository
                                                .insert(floorPlanEntity!!.copy(defectListId = defectListEntity.id))
                                                .map { result: Result<Long> ->
                                                    result.getOrThrow()
                                                }
                                                .map { floorPlanEntityId ->
                                                    defectListEntity.copy(floorPlanEntity = floorPlanEntity
                                                            .copy(defectListId = defectListEntity.id),
                                                                          id = floorPlanEntityId)
                                                }
                                    }
                                    .map { defectListEntity ->
                                        Result.success(defectListEntity)
                                    }
                                    .singleOrError()
                        })
                                .onErrorReturn { Result.failure(Error.DatabaseError("")) }
                    }

    fun getAll(): Observable<Result<List<DefectListEntity>>> {

        return defectListLocalDataSource
                .getDefectListWithRelations()
                .map { defectListWithStreetAddressList ->

                    defectListWithStreetAddressList
                            .map { defectListWithStreetAddress ->
                                val defectListEntity = defectListWithStreetAddress.defectListEntity
                                val streetAddressEntity = defectListWithStreetAddress.streetAddressWithFloor
                                        .first().streetAddressEntity
                                val viewParticipantEntityList = defectListWithStreetAddress.viewParticipantEntityList
                                val floorPlanEntity = defectListWithStreetAddress.floorPlanEntity.first()

                                val newDefectListEntity = defectListEntity
                                        .copy(streetAddressEntity = streetAddressEntity,
                                              viewParticipantEntityList = viewParticipantEntityList,
                                              floorPlanEntity = floorPlanEntity)

                                newDefectListEntity
                            }


                }
                .map { Result.success(it) }
                .toObservable()
                .onErrorReturn { Result.failure(Error.DatabaseError("")) }

    }


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
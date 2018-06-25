package de.sevennerds.trackdefects.data.defect_list

import de.sevennerds.trackdefects.data.LocalDataSource
import de.sevennerds.trackdefects.data.defect_list.local.DefectListLocalDataSource
import de.sevennerds.trackdefects.data.response.Result
import io.reactivex.Single
import java.util.concurrent.Callable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefectListRepository @Inject constructor(
        private val defectListLocal: DefectListLocalDataSource,
        private val localDataSource: LocalDataSource
) {

    fun insert(defectListEntity: DefectListEntity): Single<Result<Long>> {
        return Single.fromCallable {
            localDataSource.runInTransaction (
                Callable {
                    val defectListEntityId = defectListLocal.insert(defectListEntity)
                    Result.Success(defectListEntityId)
                }
            )
        }
    }



    /*
    fun insertBasic(defectListEntity: DefectListEntity): Single<Result<DefectListEntity>> =
            Single.fromCallable {

                val streetAddress = defectListEntity.streetAddressEntity!!
                val viewParticipantList = streetAddress.viewParticipantEntityList

                var defectListEntityNew: DefectListEntity

                localDataSource.runInTransaction(Callable {
                    val defectListId = defectListLocal.insert(defectListEntity)

                    val streetAddressId = streetAddressLocalDao.insert(streetAddress
                            .copy(defectListId = defectListId))


                    val viewParticipantIdList = viewParticipantLocalDao.insert(viewParticipantList
                            .map { it.copy(streetAddressId = streetAddressId) })

                    val viewParticipantNewList = viewParticipantList
                            .mapIndexed { index, viewParticipant ->
                                viewParticipant
                                        .copy(id = viewParticipantIdList[index],
                                                streetAddressId = streetAddressId)
                            }

                    val streetAddressNew = streetAddress.copy(
                            id = streetAddressId,
                            defectListId = defectListId,
                            viewParticipantEntityList = viewParticipantNewList)

                    defectListEntityNew = defectListEntity.copy(
                            id = defectListId,
                            streetAddressEntity = streetAddressNew)

                    Result.Success(defectListEntityNew)
                })
            }
            */
}

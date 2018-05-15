package de.sevennerds.trackdefects.data.defect_list

import de.sevennerds.trackdefects.data.LocalDataSource
import de.sevennerds.trackdefects.data.defect_list.local.DefectListLocalDataSource
import de.sevennerds.trackdefects.data.response.Response
import de.sevennerds.trackdefects.data.street_address.StreetAddressLocalDataSource
import de.sevennerds.trackdefects.data.view_participant.ViewParticipantLocalDataSource
import io.reactivex.Single
import java.util.concurrent.Callable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefectListRepo @Inject constructor(private val defectListLocal: DefectListLocalDataSource,
                                         private val streetAddressLocal: StreetAddressLocalDataSource,
                                         private val viewParticipantLocal: ViewParticipantLocalDataSource,
                                         private val localDataSource: LocalDataSource) {

    fun insertBasic(defectList: DefectList): Single<Response<DefectList>> =
            Single.fromCallable {

                val streetAddress = defectList.streetAddress!!
                val viewParticipantList = streetAddress.viewParticipantList

                var defectListNew: DefectList

                localDataSource.runInTransaction(Callable {
                    val defectListId = defectListLocal.insert(defectList)

                    val streetAddressId = streetAddressLocal.insert(streetAddress
                            .copy(defectListId = defectListId))


                    val viewParticipantIdList = viewParticipantLocal.insert(viewParticipantList
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
                            viewParticipantList = viewParticipantNewList)

                    defectListNew = defectList.copy(
                            id = defectListId,
                            streetAddress = streetAddressNew)

                    Response.Success(defectListNew)
                })
            }

}

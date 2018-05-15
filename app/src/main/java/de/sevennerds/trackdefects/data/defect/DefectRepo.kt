package de.sevennerds.trackdefects.data.defect

import de.sevennerds.trackdefects.data.LocalDataSource
import de.sevennerds.trackdefects.data.defect_image.DefectImageLocalDataSource
import de.sevennerds.trackdefects.data.defect_info.DefectInfoLocalDataSource
import de.sevennerds.trackdefects.data.floor.FloorLocalDataSource
import de.sevennerds.trackdefects.data.living_unit.LivingUnitLocalDataSource
import de.sevennerds.trackdefects.data.response.Response
import de.sevennerds.trackdefects.data.room.RoomLocalDataSource
import io.reactivex.Single
import java.util.concurrent.Callable
import javax.inject.Inject

class DefectRepo @Inject constructor(
        private val localDataSource: LocalDataSource
        private val floorLocalDataSource: FloorLocalDataSource,
        private val livingUnitLocalDataSource: LivingUnitLocalDataSource,
        private val roomLocalDataSource: RoomLocalDataSource,
        private val defectInfoLocalDataSource: DefectInfoLocalDataSource,
        private val defectImageLocalDataSource: DefectImageLocalDataSource) {

    fun insert(defectInsert: DefectInsert): Single<Response<Unit>> =

            Single.fromCallable {
                localDataSource.runInTransaction(Callable {
                    val (streetAddressId,
                            floor,
                            livingUnit,
                            room,
                            defectInfo,
                            defectImageList) = defectInsert

                    val floorId = floorLocalDataSource.insert(floor
                            .copy(streetAddressId = streetAddressId))

                    val livingUnitId = livingUnitLocalDataSource.insert(livingUnit
                            .copy(floorId = floorId))

                    val roomId = roomLocalDataSource.insert(room
                            .copy(livingUnitId = livingUnitId))

                    val defectInfoId = defectInfoLocalDataSource.insert(defectInfo
                            .copy(roomId = roomId))

                    val defectImageIdList = defectImageLocalDataSource.insert(defectImageList
                            .map { it.copy(defectInfoId = defectInfoId) })


                    Response.Success(Unit)
                })
            }
}
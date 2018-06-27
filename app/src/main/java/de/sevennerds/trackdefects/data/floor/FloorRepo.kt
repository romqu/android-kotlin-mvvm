package de.sevennerds.trackdefects.data.floor

import de.sevennerds.trackdefects.data.LocalDataSource
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FloorRepo @Inject constructor(
        private val floorLocalDataSource: FloorLocalDataSource,
        private val localDataSource: LocalDataSource
) {
    fun insertOrUpdate(floorEntity: FloorEntity) =
            Single.fromCallable {
                if (floorEntity.id == 0L) {
                    insert(floorEntity)
                }
            }

    fun insert(floorEntity: FloorEntity) =
            Single.fromCallable {
                floorLocalDataSource.insert(floorEntity)
            }

    fun update(floorEntity: FloorEntity) =
            Single.fromCallable {
                floorLocalDataSource.update(floorEntity)
            }
}
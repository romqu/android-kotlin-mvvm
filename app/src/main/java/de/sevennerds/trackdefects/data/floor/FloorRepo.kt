package de.sevennerds.trackdefects.data.floor

import de.sevennerds.trackdefects.data.LocalDataSource
import io.reactivex.Single
import java.util.concurrent.Callable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FloorRepo @Inject constructor(private val floorLocalDataSource: FloorLocalDataSource,
                                    private val localDataSource: LocalDataSource) {

    fun insertOrUpdate(floor: Floor) =
            Single.fromCallable {
                if (floor.id == 0L){
                    insert(floor)
                }
            }

    fun insert(floor: Floor) =
            Single.fromCallable {
                floorLocalDataSource.insert(floor)
            }

    fun update(floor: Floor) =
            Single.fromCallable {
                floorLocalDataSource.update(floor)
            }
}
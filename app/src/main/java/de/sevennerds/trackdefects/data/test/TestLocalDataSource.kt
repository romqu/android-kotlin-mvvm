package de.sevennerds.trackdefects.data.test

import androidx.room.*
import de.sevennerds.trackdefects.data.client.ClientEntity
import de.sevennerds.trackdefects.data.client.local.ClientLocalDataSource
import io.reactivex.Flowable
import io.reactivex.Single

@Database(entities = [
    TestEntity::class],
          version = 1)
abstract class TestLocalDb: RoomDatabase() {

    abstract fun test(): TestLocalData
}


@Dao
interface TestLocalData {
    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    fun insert(testEntity: TestEntity)

    @get:Query("SELECT * FROM test_table LIMIT 1")
    val one: Single<TestEntity>
}
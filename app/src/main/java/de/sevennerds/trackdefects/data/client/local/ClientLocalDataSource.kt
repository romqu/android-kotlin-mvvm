package de.sevennerds.trackdefects.data.client.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.OnConflictStrategy.ROLLBACK
import androidx.room.Query
import de.sevennerds.trackdefects.data.client.ClientEntity
import io.reactivex.Flowable

@Dao
interface ClientLocalDataSource {

    @get:Query("SELECT * FROM client LIMIT 1")
    val one: Flowable<ClientEntity>

    @Insert
    fun insertAll(vararg posts: ClientEntity)

    @Insert(onConflict = ROLLBACK)
    fun insert(clientEntity: ClientEntity)

}
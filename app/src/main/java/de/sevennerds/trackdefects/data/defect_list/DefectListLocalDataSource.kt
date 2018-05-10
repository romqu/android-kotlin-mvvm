package de.sevennerds.trackdefects.data.defect_list

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.sevennerds.trackdefects.data.defect_list.relation.DefectListWithStreetAddress
import io.reactivex.Flowable

@Dao
interface DefectListLocalDataSource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(defectList: DefectList)

    @Query("SELECT * FROM defect_list")
    fun getDefectListWithStreetAddress(): Flowable<List<DefectListWithStreetAddress>>
}
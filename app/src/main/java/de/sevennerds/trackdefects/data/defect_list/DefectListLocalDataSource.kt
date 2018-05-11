package de.sevennerds.trackdefects.data.defect_list

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import de.sevennerds.trackdefects.data.BaseLocalDataSource
import de.sevennerds.trackdefects.data.defect_list.relation.DefectListWithStreetAddress
import io.reactivex.Flowable

@Dao
interface DefectListLocalDataSource : BaseLocalDataSource<DefectList> {

    @Transaction
    @Query("SELECT * FROM defect_list")
    fun getDefectListWithRelations(): Flowable<List<DefectListWithStreetAddress>>


}
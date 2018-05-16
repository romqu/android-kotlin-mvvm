package de.sevennerds.trackdefects.data.defect_list.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import de.sevennerds.trackdefects.data.BaseLocalDataSource
import de.sevennerds.trackdefects.data.defect_list.DefectList
import de.sevennerds.trackdefects.data.defect_list.local.relation.DefectListWithStreetAddress
import io.reactivex.Flowable

@Dao
abstract class DefectListLocalDataSource : BaseLocalDataSource<DefectList> {

    @Transaction
    @Query("SELECT * FROM defect_list")
    abstract fun getDefectListWithRelations(): Flowable<List<DefectListWithStreetAddress>>


}
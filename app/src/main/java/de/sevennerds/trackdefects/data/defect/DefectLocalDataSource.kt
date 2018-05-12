package de.sevennerds.trackdefects.data.defect

import androidx.room.Dao
import de.sevennerds.trackdefects.data.BaseLocalDataSource

@Dao
interface DefectLocalDataSource : BaseLocalDataSource<Defect> {

}
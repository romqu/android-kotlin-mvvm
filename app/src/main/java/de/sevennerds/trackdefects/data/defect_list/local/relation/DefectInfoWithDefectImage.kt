package de.sevennerds.trackdefects.data.defect_list.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import de.sevennerds.trackdefects.data.defect_image.DefectImageEntity
import de.sevennerds.trackdefects.data.defect_info.DefectInfoEntity

class DefectInfoWithDefectImage {

    @Embedded
    lateinit var defectInfoEntity: DefectInfoEntity

    @Relation(parentColumn = "id",
            entityColumn = "defect_info_id", entity = DefectImageEntity::class)
    lateinit var defectImageEntityList: List<DefectImageEntity>
}
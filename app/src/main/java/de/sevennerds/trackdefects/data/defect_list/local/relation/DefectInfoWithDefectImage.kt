package de.sevennerds.trackdefects.data.defect_list.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import de.sevennerds.trackdefects.data.defect_image.DefectImage
import de.sevennerds.trackdefects.data.defect_info.DefectInfo

class DefectInfoWithDefectImage {

    @Embedded
    lateinit var defectInfo: DefectInfo

    @Relation(parentColumn = "id",
            entityColumn = "defect_info_id", entity = DefectImage::class)
    lateinit var defectImageList: List<DefectImage>
}
package de.sevennerds.trackdefects.data.defect_list.relation

import androidx.room.Embedded
import androidx.room.Relation
import de.sevennerds.trackdefects.data.defect.Defect
import de.sevennerds.trackdefects.data.defect_image.DefectImage

class DefectWithDefectImage {

    @Embedded
    lateinit var defect: Defect

    @Relation(parentColumn = "id",
            entityColumn = "defect_id", entity = DefectImage::class)
    lateinit var defectImageList: List<DefectImage>
}
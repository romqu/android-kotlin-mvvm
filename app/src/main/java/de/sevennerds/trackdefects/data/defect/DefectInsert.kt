package de.sevennerds.trackdefects.data.defect

import de.sevennerds.trackdefects.data.defect_image.DefectImage
import de.sevennerds.trackdefects.data.defect_info.DefectInfo
import de.sevennerds.trackdefects.data.floor.Floor
import de.sevennerds.trackdefects.data.living_unit.LivingUnit
import de.sevennerds.trackdefects.data.room.Room

data class DefectInsert(val streetAddressId: Long,
                        val floor: Floor,
                        val livingUnit: LivingUnit,
                        val room: Room,
                        val defectInfo: DefectInfo,
                        val defectImageList: List<DefectImage>)
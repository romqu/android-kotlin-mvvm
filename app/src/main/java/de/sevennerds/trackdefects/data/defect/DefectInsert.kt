package de.sevennerds.trackdefects.data.defect

import de.sevennerds.trackdefects.data.defect_image.DefectImageEntity
import de.sevennerds.trackdefects.data.defect_info.DefectInfoEntity
import de.sevennerds.trackdefects.data.floor.FloorEntity
import de.sevennerds.trackdefects.data.living_unit.LivingUnitEntity
import de.sevennerds.trackdefects.data.room.RoomEntity

data class DefectInsert(
        val streetAddressId: Long,
        val floorEntity: FloorEntity,
        val livingUnitEntity: LivingUnitEntity,
        val roomEntity: RoomEntity,
        val defectInfoEntity: DefectInfoEntity,
        val defectImageEntityList: List<DefectImageEntity>
)
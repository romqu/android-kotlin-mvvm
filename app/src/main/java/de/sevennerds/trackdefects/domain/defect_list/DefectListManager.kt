package de.sevennerds.trackdefects.domain.defect_list

import javax.inject.Inject

class DefectListManager @Inject constructor() {

    fun insert() {
        /**
         *      Insert DefectList with related entities
         *
         *          - InsertDefectListEntity
         *          - InsertStreetAddressEntity
         *          - InsertFloorPlanEntity
         */
    }

    fun insertAll() {
        /**
         *      Convenience method to insert List of DefectList with related entities.
         */
    }

}
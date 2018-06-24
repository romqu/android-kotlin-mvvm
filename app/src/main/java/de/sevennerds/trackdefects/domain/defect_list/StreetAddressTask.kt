package de.sevennerds.trackdefects.domain.defect_list

import de.sevennerds.trackdefects.data.street_address.StreetAddressRepository
import javax.inject.Inject

class StreetAddressTask @Inject constructor(
        private val streetAddressRepository: StreetAddressRepository
) {
    fun insertStreetAddressEntity() {

    }

    fun insertStreetAddressEntityAll() {

    }
}
package de.sevennerds.trackdefects.presentation.select_contacts

import com.wafflecopter.multicontactpicker.ContactResult

sealed class SelectContactsEvent {

    data class SelectContactEvent(val contactResultList: List<ContactResult>,
                                  val currentContactModelList: List<ContactModel>) : SelectContactsEvent()

    data class RemoveContactEvent(val contactPosition: Int,
                                  val currentContactModelList: List<ContactModel>) : SelectContactsEvent()
}
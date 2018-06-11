package de.sevennerds.trackdefects.presentation.select_contacts

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.wafflecopter.multicontactpicker.ContactResult
import kotlinx.android.parcel.Parcelize

sealed class SelectContactsEvent {

    data class SelectContactEvent(val contactResultList: List<ContactResult>,
                                  val currentContactModelList: List<ContactModel>) : SelectContactsEvent()

    data class RemoveContactEvent(val contactPosition: Int,
                                  val currentContactModelList: List<ContactModel>) : SelectContactsEvent()

    object Init : SelectContactsEvent()
}

sealed class ViewResult {
    data class SelectContact(val diffResult: DiffUtil.DiffResult,
                             val contactModelList: List<ContactModel>) : ViewResult()

    data class RemoveContact(val contactModelList: List<ContactModel>,
                             val diffResult: DiffUtil.DiffResult) : ViewResult()

    object Init : ViewResult()
}

sealed class ViewRenderState {
    data class SelectContact(val contactModelList: List<ContactModel>,
                             val diffResult: DiffUtil.DiffResult) : ViewRenderState()

    data class RemoveContact(val contactModelList: List<ContactModel>,
                             val diffResult: DiffUtil.DiffResult) : ViewRenderState()

    data class  Init(val contactModelList: List<ContactModel>) : ViewRenderState()
}

data class ViewState(val contactModelList: List<ContactModel>,
                     val diffResult: DiffUtil.DiffResult?) {
    companion object {
        fun initial() = ViewState(emptyList(), null)
    }
}

@Parcelize
data class ViewStateP(val contactModelList: List<ContactModel>) : Parcelable
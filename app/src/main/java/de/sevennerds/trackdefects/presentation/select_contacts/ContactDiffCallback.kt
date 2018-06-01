package de.sevennerds.trackdefects.presentation.select_contacts

import androidx.recyclerview.widget.DiffUtil
import ir.mirrajabi.rxcontacts.Contact

class ContactDiffCallback(private val oldContactList: List<ContactModel>,
                          private val newContactList: List<ContactModel>) : DiffUtil.Callback() {


    override fun getOldListSize() = oldContactList.size

    override fun getNewListSize() = newContactList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldContactList[oldItemPosition].name == newContactList[newItemPosition].name

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldContactList[oldItemPosition] == newContactList[newItemPosition]

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}
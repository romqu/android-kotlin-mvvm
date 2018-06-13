package de.sevennerds.trackdefects.presentation.base

import androidx.recyclerview.widget.DiffUtil

class BaseDiffCallback<T : IDiffable>(private val oldList: List<T>,
                                                                                   private val newList: List<T>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].uuid == newList[newItemPosition].uuid

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }

}

interface IDiffable {
    val uuid: String
}
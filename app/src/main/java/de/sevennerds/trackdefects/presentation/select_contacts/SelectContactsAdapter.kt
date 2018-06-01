package de.sevennerds.trackdefects.presentation.select_contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.sevennerds.trackdefects.R
import kotlinx.android.synthetic.main.item_select_contacts.view.*


class SelectContactsAdapter(private val contactModelList: MutableList<ContactModel>) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater
                               .from(parent.context)
                               .inflate(R.layout.item_select_contacts,
                                        parent,
                                        false))



    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            holder.bind(contactModelList[position])

    override fun getItemCount(): Int =
            contactModelList.size

    fun getList() = contactModelList

    fun addAllToList(newContactModelList: List<ContactModel>) {
        this.contactModelList.addAll(newContactModelList)
    }
    
    fun clearList() = contactModelList.clear()
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(contactModel: ContactModel) = with(contactModel) {
        itemView.text_view_select_contacts.text = name
    }
}
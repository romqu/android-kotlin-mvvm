package de.sevennerds.trackdefects.presentation.select_contacts.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.view.clicks
import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.presentation.select_contacts.ContactModel
import io.reactivex.BackpressureStrategy
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_select_contacts.view.*


class SelectContactsAdapter(private val contactModelList: MutableList<ContactModel>)
    : RecyclerView.Adapter<ViewHolder>() {

    val onClickSubject: PublishSubject<Int> = PublishSubject.create()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater
                               .from(parent.context)
                               .inflate(R.layout.item_select_contacts,
                                        parent,
                                        false),
                       onClickSubject,
                       compositeDisposable)


    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            holder.bind(contactModelList[position])

    override fun getItemCount(): Int =
            contactModelList.size

    fun getList() = contactModelList

    fun addAllToList(newContactModelList: List<ContactModel>) {
        this.contactModelList.addAll(newContactModelList)
    }

    fun clearList() = contactModelList.clear()

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

        compositeDisposable.clear()
    }
}

class ViewHolder(private val view: View,
                 private val onClickSubject: PublishSubject<Int>,
                 private val compositeDisposable: CompositeDisposable) : RecyclerView.ViewHolder(view) {




    init {
        compositeDisposable += view.clicks()
                .subscribe { onClickSubject.onNext(adapterPosition) }
    }

    fun bind(contactModel: ContactModel) = with(contactModel) {
        itemView.text_view_select_contacts.text = name
    }
}
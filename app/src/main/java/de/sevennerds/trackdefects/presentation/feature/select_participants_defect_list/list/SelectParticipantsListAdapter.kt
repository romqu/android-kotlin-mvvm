package de.sevennerds.trackdefects.presentation.feature.select_participants_defect_list.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.view.clicks
import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.presentation.feature.select_participants_defect_list.ParticipantModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_select_contacts.view.*


class SelectParticipantsListAdapter(private val participantModelList: MutableList<ParticipantModel>)
    : RecyclerView.Adapter<ViewHolder>() {

    private val onItemClickSubject: PublishSubject<Int> = PublishSubject.create()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.item_select_contacts,
                            parent,
                            false),
                    onItemClickSubject,
                    compositeDisposable)


    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            holder.bind(participantModelList[position])

    override fun getItemCount(): Int =
            participantModelList.size

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

        compositeDisposable.clear()
    }

    fun getList() = participantModelList.toList()

    fun addAllToList(newParticipantModelList: List<ParticipantModel>) {
        this.participantModelList.addAll(newParticipantModelList)
    }

    fun clearList() = participantModelList.clear()

    fun getOnItemClickListener(): Observable<Int> =
            onItemClickSubject
                    .toFlowable(BackpressureStrategy.BUFFER)
                    .toObservable()
}

class ViewHolder(view: View,
                 private val onClickSubject: PublishSubject<Int>,
                 private val compositeDisposable: CompositeDisposable) : RecyclerView.ViewHolder(view) {

    init {
        compositeDisposable += view
                .clicks()
                .subscribe { onClickSubject.onNext(adapterPosition) }
    }

    fun bind(participantModel: ParticipantModel) =
            with(participantModel) {
                itemView.text_view_select_contacts.text = name
            }
}
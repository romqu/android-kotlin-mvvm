package de.sevennerds.trackdefects.presentation.feature.display_defect_lists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.view.clicks
import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.common.toObservable
import de.sevennerds.trackdefects.presentation.model.DefectListModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_display_defects_list.view.*

class DisplayDefectListsAdapter(private val defectListModelList: MutableList<DefectListModel>)
    : RecyclerView.Adapter<ViewHolder>() {

    private val onItemClickSubject: PublishSubject<Int> = PublishSubject.create()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater
                               .from(parent.context)
                               .inflate(R.layout.item_display_defects_list,
                                        parent,
                                        false),
                       onItemClickSubject,
                       compositeDisposable)


    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            holder.bind(defectListModelList[position])

    override fun getItemCount(): Int =
            defectListModelList.size

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

        compositeDisposable.clear()
    }

    fun getList() = defectListModelList.toList()

    fun addAllToList(defectListModelList: List<DefectListModel>) {
        this.defectListModelList.addAll(defectListModelList)
    }

    fun clearList() = defectListModelList.clear()

    fun getOnItemClickListener(): Observable<Int> =
            onItemClickSubject.toObservable()
}

class ViewHolder(view: View,
                 private val onClickSubject: PublishSubject<Int>,
                 private val compositeDisposable: CompositeDisposable) : RecyclerView.ViewHolder(view) {

    init {
        compositeDisposable += view
                .clicks()
                .subscribe { onClickSubject.onNext(adapterPosition) }
    }

    fun bind(defectListModel: DefectListModel) {
        with(defectListModel) {
            itemView.displayDefectsItemTextView.text =
                    defectListModel.streetAddressModel.name
        }
    }
}
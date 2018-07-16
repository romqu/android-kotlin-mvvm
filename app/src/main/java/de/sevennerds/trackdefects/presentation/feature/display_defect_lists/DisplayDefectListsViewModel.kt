package de.sevennerds.trackdefects.presentation.feature.display_defect_lists

import de.sevennerds.trackdefects.data.response.Result
import de.sevennerds.trackdefects.domain.feature.load_defect_list.LoadDefectListsManager
import de.sevennerds.trackdefects.presentation.base.BaseViewModel
import de.sevennerds.trackdefects.presentation.model.DefectListModel
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DisplayDefectListsViewModel @Inject constructor(
        private val loadDefectListsManager: LoadDefectListsManager)
    : BaseViewModel<DisplayDefectListsView.Event, DisplayDefectListsView.State>() {

    override val eventToViewState = ObservableTransformer<DisplayDefectListsView.Event,
            DisplayDefectListsView.State> { upstream ->

        upstream.observeOn(Schedulers.io())
                .publish { shared ->
                    Observable.mergeArray(
                            shared.ofType(
                                    DisplayDefectListsView.Event.Init::class.java)
                                    .compose(initEventToResult),
                            shared.ofType(DisplayDefectListsView.Event.Add::class.java)
                                    .compose(addEventToResult)
                    ).compose(resultToViewState)
                }
    }


    // TODO move scan part to domain
    private val initEventToResult = ObservableTransformer<DisplayDefectListsView.Event.Init,
            DisplayDefectListsView.Result> { upstream ->

        upstream.flatMap {

            loadDefectListsManager
                    .execute()
        }.scan(emptyList()) { previousList: List<DefectListModel>, result: Result<DefectListModel> ->

            previousList.union(
                    listOf(result.getOrThrow()))
                    .toList()
        }.map { defectListModelList ->
            DisplayDefectListsView.Result.Init(defectListModelList)
        }

    }

    private val addEventToResult = ObservableTransformer<DisplayDefectListsView.Event.Add,
            DisplayDefectListsView.Result> { upstream ->

        upstream.map { DisplayDefectListsView.Result.Add }
    }

    private val resultToViewState = ObservableTransformer<DisplayDefectListsView.Result,
            DisplayDefectListsView.State> { upstream ->

        upstream.scan(DisplayDefectListsView.State.initial()) { previousState,
                                                                result ->

            when (result) {
                is DisplayDefectListsView.Result.Init ->
                    previousState.copy(renderState = DisplayDefectListsView
                            .RenderState.Init(result.defectListModelList))
                is DisplayDefectListsView.Result.InitError ->
                    previousState.copy(renderState = DisplayDefectListsView
                            .RenderState.None)
                is DisplayDefectListsView.Result.Add ->
                    previousState.copy(renderState = DisplayDefectListsView
                            .RenderState.Add)
            }

        }

    }

}
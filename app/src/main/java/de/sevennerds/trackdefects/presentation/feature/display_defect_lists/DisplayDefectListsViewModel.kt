package de.sevennerds.trackdefects.presentation.feature.display_defect_lists

import com.orhanobut.logger.Logger
import de.sevennerds.trackdefects.domain.feature.load_defect_list.LoadDefectListsManager
import de.sevennerds.trackdefects.presentation.base.BaseViewModel
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
                    Observable.mergeArray(shared.ofType(
                            DisplayDefectListsView.Event.Init::class.java)
                                                  .compose(initEventToResult))
                            .compose(resultToViewState)
                }

    }


    private val initEventToResult = ObservableTransformer<DisplayDefectListsView.Event.Init,
            DisplayDefectListsView.Result> { upstream ->

        upstream.flatMap {

            loadDefectListsManager
                    .execute()
        }
                .map { DisplayDefectListsView.Result.Init(listOf(it)) }

    }

    private val resultToViewState = ObservableTransformer<DisplayDefectListsView.Result,
            DisplayDefectListsView.State> { upstream ->

        upstream.scan(DisplayDefectListsView.State.initial()) { previousState,
                                                                result ->

            when (result) {
                is DisplayDefectListsView.Result.Init ->
                    previousState.copy(renderState = DisplayDefectListsView
                            .RenderState.Init(result.defectListModelList))
            }

        }

    }

}
package de.sevennerds.trackdefects.domain.feature.load_defect_list

import com.orhanobut.logger.Logger
import de.sevennerds.trackdefects.common.Constants
import de.sevennerds.trackdefects.data.defect_list.DefectListRepository
import de.sevennerds.trackdefects.data.response.Result
import de.sevennerds.trackdefects.domain.mapper.DefectListMapper
import de.sevennerds.trackdefects.presentation.model.DefectListModel
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadDefectListsManager @Inject constructor(
        private val defectListRepository: DefectListRepository,
        private val loadPictureTask: LoadPictureTask) {

    fun execute(): Observable<Result<DefectListModel>> = defectListRepository
            .getAll()
            .flatMap { result ->

                val defectListEntityList = result.getOrThrow()

                Observable
                        .fromIterable(defectListEntityList)
            }
            .flatMapSingle { defectListEntity ->
                val fileName = defectListEntity.floorPlanEntity!!
                        .fileName

                loadPictureTask
                        .execute(fileName, Constants.PROJECTS_PATH)
                        .map { Pair(defectListEntity, it.getOrThrow()) }
            }
            .map { pair ->
                Result.success(DefectListMapper
                        .entityToModel(pair.first, pair.second))
            }

}
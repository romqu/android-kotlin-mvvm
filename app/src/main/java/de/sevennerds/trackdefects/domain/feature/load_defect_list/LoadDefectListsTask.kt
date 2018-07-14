package de.sevennerds.trackdefects.domain.feature.load_defect_list

import de.sevennerds.trackdefects.data.defect_list.DefectListEntity
import de.sevennerds.trackdefects.data.defect_list.DefectListRepository
import de.sevennerds.trackdefects.data.response.Result
import de.sevennerds.trackdefects.presentation.model.DefectListModel
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadDefectListsTask @Inject constructor(
        private val defectListRepository: DefectListRepository) {

    fun execute(): Single<Result<List<DefectListEntity>>> {
        return defectListRepository
                .getAll()
                .map { result ->



                    result
                }
    }

}
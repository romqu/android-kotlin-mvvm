package de.sevennerds.trackdefects.domain.feature.save_picture_disk

import android.graphics.Bitmap
import de.sevennerds.trackdefects.data.file.FileEntity
import de.sevennerds.trackdefects.data.file.FileRepository
import de.sevennerds.trackdefects.data.response.Result
import de.sevennerds.trackdefects.presentation.model.FileModel
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavePictureDiskTask @Inject constructor(
        private val fileRepository: FileRepository) {

    fun execute(fileModel: FileModel<Bitmap>): Single<Result<FileEntity<Bitmap>>> {
        return fileRepository
                .save(FileEntity(fileModel.name,
                                 fileModel.data,
                                 fileModel.path))
    }
}
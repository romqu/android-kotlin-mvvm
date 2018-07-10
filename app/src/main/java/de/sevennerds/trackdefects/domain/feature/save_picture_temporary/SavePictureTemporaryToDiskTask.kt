package de.sevennerds.trackdefects.domain.feature.save_picture_temporary

import android.graphics.Bitmap
import de.sevennerds.trackdefects.common.Constants
import de.sevennerds.trackdefects.data.file.FileEntity
import de.sevennerds.trackdefects.data.file.FileRepository
import de.sevennerds.trackdefects.presentation.model.FileModel
import de.sevennerds.trackdefects.util.getUuidV4
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavePictureTemporaryToDiskTask @Inject constructor(
        private val fileRepo: FileRepository) {

    fun execute(fileBitmapModel: FileModel<Bitmap>) =
            fileRepo.saveTemporary(FileEntity(
                    "${getUuidV4()}${Constants.JPEG_POSTFIX}",
                    fileBitmapModel.data))
}
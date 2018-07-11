package de.sevennerds.trackdefects.domain.feature.save_picture_disk

import android.graphics.Bitmap
import de.sevennerds.trackdefects.data.file.FileRepository
import de.sevennerds.trackdefects.presentation.model.FileModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavePictureDisk @Inject constructor(
        fileRepository: FileRepository) {

    fun execute(fileModel: FileModel<Bitmap>) {

    }
}
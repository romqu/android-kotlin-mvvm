package de.sevennerds.trackdefects.domain

import android.graphics.Bitmap
import de.sevennerds.trackdefects.data.file.FileEntity
import de.sevennerds.trackdefects.data.file.FileRepository
import de.sevennerds.trackdefects.presentation.model.FileModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveBitmapToDiskTask @Inject constructor(
        private val fileRepo: FileRepository) {

    fun execute(fileBitmapModel: FileModel<Bitmap>) =
            fileRepo.save(FileEntity(fileBitmapModel.name,
                                     fileBitmapModel.data))
}
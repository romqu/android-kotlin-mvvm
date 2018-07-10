package de.sevennerds.trackdefects.domain.feature.load_temporary_picture

import android.graphics.Bitmap
import de.sevennerds.trackdefects.data.file.FileRepository
import de.sevennerds.trackdefects.data.response.Result
import de.sevennerds.trackdefects.presentation.model.FileModel
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadTemporaryPictureTask @Inject constructor(
        private val fileRepository: FileRepository) {

    fun execute(fileName: String): Single<Result<FileModel<Bitmap>>> =
            fileRepository
                    .loadTemporaryImage(fileName)
                    .map { result ->
                        result.onSuccess { fileEnityBitmap ->
                            FileModel(fileEnityBitmap.name,
                                      fileEnityBitmap.data)
                        }
                    }

}
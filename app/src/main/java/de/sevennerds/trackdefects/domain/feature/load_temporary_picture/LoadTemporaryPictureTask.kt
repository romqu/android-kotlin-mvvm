package de.sevennerds.trackdefects.domain.feature.load_temporary_picture

import android.graphics.Bitmap
import de.sevennerds.trackdefects.common.asSingle
import de.sevennerds.trackdefects.data.cache.BitmapCache
import de.sevennerds.trackdefects.data.file.FileRepository
import de.sevennerds.trackdefects.data.response.Result
import de.sevennerds.trackdefects.presentation.model.FileModel
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadTemporaryPictureTask @Inject constructor(
        private val fileRepository: FileRepository,
        private val bitmapCache: BitmapCache) {

    // TODO insert into cache if from disk
    fun execute(fileName: String): Single<Result<FileModel<Bitmap>>> {

        return Single
                .merge(bitmapCache
                               .get(fileName)
                               .asSingle()
                               .map { result ->
                                   result.onSuccessResult { bitmap ->
                                       FileModel(fileName, bitmap)
                                   }
                               },
                       fileRepository
                               .loadTemporaryImage(fileName)
                               .map { result ->
                                   result.onSuccessResult { fileEnityBitmap ->
                                       FileModel(fileEnityBitmap.name,
                                                 fileEnityBitmap.data)
                                   }
                               }
                )
                .filter { result ->
                    result.match({
                                     true
                                 },
                                 {
                                     false
                                 })
                }
                .take(1)
                .singleOrError()
    }
}
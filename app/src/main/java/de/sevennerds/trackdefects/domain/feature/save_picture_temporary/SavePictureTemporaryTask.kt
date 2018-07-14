package de.sevennerds.trackdefects.domain.feature.save_picture_temporary

import android.graphics.Bitmap
import de.sevennerds.trackdefects.common.Constants
import de.sevennerds.trackdefects.data.cache.BitmapCache
import de.sevennerds.trackdefects.data.file.FileEntity
import de.sevennerds.trackdefects.data.file.FileRepository
import de.sevennerds.trackdefects.data.response.Result
import de.sevennerds.trackdefects.presentation.model.FileModel
import de.sevennerds.trackdefects.util.getUuidV4
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavePictureTemporaryTask @Inject constructor(
        private val fileRepo: FileRepository, private val bitmapCache: BitmapCache) {

    fun execute(fileBitmapModel: FileModel<Bitmap>): Single<Result<FileEntity<Bitmap>>> =
            fileRepo.saveTemporary(
                    FileEntity(
                            "${getUuidV4()}${Constants.JPEG_FILE_EXTENSION}",
                            fileBitmapModel.data))
                    .map { result ->

                        result.onSuccessResult { fileEntityBitmap ->
                            bitmapCache.clearAndInsert(fileEntityBitmap.name,
                                                       fileEntityBitmap.data)
                            fileEntityBitmap
                        }
                    }
}
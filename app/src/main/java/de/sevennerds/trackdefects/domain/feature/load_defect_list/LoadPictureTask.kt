package de.sevennerds.trackdefects.domain.feature.load_defect_list

import android.graphics.Bitmap
import de.sevennerds.trackdefects.data.file.FileEntity
import de.sevennerds.trackdefects.data.file.FileRepository
import de.sevennerds.trackdefects.data.response.Result
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadPictureTask @Inject constructor(private val fileRepository: FileRepository){

    fun execute(name: String, path: String): Single<Result<FileEntity<Bitmap>>> {
        return fileRepository
                .load(path, name)
    }
}
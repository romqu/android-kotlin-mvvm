package de.sevennerds.trackdefects.data.file

import android.graphics.Bitmap
import com.orhanobut.logger.Logger
import de.sevennerds.trackdefects.common.Constants.Database.DUPLICATE_FILE
import de.sevennerds.trackdefects.common.Constants.Database.FILES_PATH
import de.sevennerds.trackdefects.common.Constants.Database.FILE_DELETED
import de.sevennerds.trackdefects.common.Constants.Database.FILE_NOT_FOUND
import de.sevennerds.trackdefects.common.Constants.Database.FILE_SAVED
import de.sevennerds.trackdefects.data.response.Error
import de.sevennerds.trackdefects.data.response.Result
import io.reactivex.Observable
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class FileRepository {

    /**
     *
     *  # Application External Storage Path
     *
     *  /sdcard/Android/data/de.sevennerds.trackdefects/<PROJECTS>/<UUID>
     *
     *  # Project structure:
     *
     *  projects/
     *      |-  361642fc-26e3-455e-860e-4c85e97b3219.jpg
     *      |-  8ef00eac-21bb-404b-9171-da5253f84346.jpg
     *      |-  cb49d296-fbc0-4f80-8a2e-1d65544ff083.jpg
     *      |-  7f1a8d62-7947-40e5-9d91-6e9c79ca3a8d.jpg
     *      '-  0a4257c9-b989-44c3-bb0e-e1106f386d44.jpg
     *
     */

    val JPEG_QUALITY = 100
    val JPEG_FILE_EXTENSION = ".jpg"

    @Suppress("UNCHECKED_CAST")
    fun load(fileName: String): Observable<Result<String>> {
        return Observable.just(fileName)
                .filter {
                    FileUtil.isReadable()
                }.flatMap {
                    BitmapUtil.fileInputStreamToBitmap(FileInputStream(File(FILES_PATH, it)))
                }.map {
                    Logger.d("Loading " + it)
                    Result.success(data = it) as Result<String>
                }.defaultIfEmpty (
                        Result.failure(Error.FileNotFoundError(FILE_NOT_FOUND))
                ).onErrorReturn {
                    Logger.d("Loading file failed: " + it.toString())
                    Result.failure(Error.FileNotFoundError(FILE_NOT_FOUND))
                }
    }

    fun save(input: GenericFile<Bitmap>): Observable<Result<String>> {
        return Observable.just(input)
                .map {
                    it -> File(FILES_PATH, it.name + JPEG_FILE_EXTENSION)
                }.filter {
                    it -> !it.exists() && FileUtil.isWritable()
                }.map {
                    it -> FileOutputStream(it)
                }.map { it ->
                    input.data.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, it)
                    it.flush()
                    it.close()
                    Logger.d("FileOutputStream: " + it.toString())
                    Result.success(FILE_SAVED)
                }.onErrorReturn {
                    Logger.d("OnErrorReturn: " + it.toString())
                    Result.failure(Error.DuplicateFileError(it.toString()))
                }.defaultIfEmpty(Result.failure(Error.DuplicateFileError(DUPLICATE_FILE)))
        }

    @Suppress("UNCHECKED_CAST")
    fun saveAll(genericFileList: List<GenericFile<Bitmap>>): Observable<Result<String>> {
        return Observable.fromArray(
                genericFileList.map {
                    it -> save(it)
                }
        ).map {
            it -> Result.success(it) as Result<String>
        }
    }

    fun delete(file: File): Observable<Result<String>> {

        /**
         *  If file does not exist, do not proceed to delete
         *  instead return an FileNotFoundError
         */

        return Observable.just(file).filter {

            /**
             *  whatever is filtered out is taken
             *  to next step .map{}
             *  In this case, every file that exists
             *  is proceeded to deletion step.
             *
             *  if expression is true proceed to next
             */

            it -> it.exists()
        }.map {
            it -> Logger.d("Deletion requested on: " + it)
            Logger.d("Request exists in filesystem: " + it.exists())
            it.delete()
            Result.success(FILE_DELETED)
        }.onErrorReturn {
            Result.failure(Error.FileNotFoundError(it.toString()))
        }.defaultIfEmpty(
            Result.failure(Error.FileNotFoundError(FILE_NOT_FOUND))
        )
    }
}
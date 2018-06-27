package de.sevennerds.trackdefects.data.file

import android.graphics.Bitmap
import com.orhanobut.logger.Logger
import de.sevennerds.trackdefects.common.Constants.Database.DELETION_FAILED
import de.sevennerds.trackdefects.common.Constants.Database.DUPLICATE_FILE
import de.sevennerds.trackdefects.common.Constants.Database.FILES_DELETED
import de.sevennerds.trackdefects.common.Constants.Database.FILES_PATH
import de.sevennerds.trackdefects.common.Constants.Database.FILE_DELETED
import de.sevennerds.trackdefects.common.Constants.Database.FILE_NOT_FOUND
import de.sevennerds.trackdefects.common.Constants.Database.SAVING_FILES_FAILED
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
                }.doOnNext {
                    Logger.d("Loading: $it")
                }.map {
                    Result.success(data = it) as Result<String>
                }.defaultIfEmpty(
                        Result.failure(Error.FileNotFoundError(FILE_NOT_FOUND))
                ).doOnError {
                    Logger.d("Loading file failed: $it")
                }.onErrorReturn {
                    Result.failure(Error.FileNotFoundError(FILE_NOT_FOUND))
                }
    }

    @Suppress("UNCHECKED_CAST")
    fun loadAll(fileNameList: List<String>): Observable<Result<String>> {
        return Observable.fromIterable(fileNameList).doOnNext {
            Logger.d("Loading file: $it")
        }.flatMap { it ->
            load(it)
        }.toList().map { it ->
            if (!it.contains(Result.failure(Error.FileNotFoundError(FILE_NOT_FOUND)))) {
                Result.success(it) as Result<String>
            } else {
                Result.failure(Error.FileNotFoundError(FILE_NOT_FOUND))
            }
        }.toObservable().onErrorReturn {
            Result.failure(Error.FileNotFoundError(FILE_NOT_FOUND))
        }.doOnError { it ->
            Logger.d("Error: $it")
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun save(input: FileEntity<Bitmap>): Observable<Result<String>> {
        return Observable.just(input)
                .map { it ->
                    File(FILES_PATH, it.name + JPEG_FILE_EXTENSION)
                }.filter { it ->
                    !it.exists() && FileUtil.isWritable()
                }.map { it ->
                    FileOutputStream(it)
                }.map { it ->
                    it.use {
                        input.data.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, it)
                        Result.success(Unit) as Result<String>
                    }
                }.doOnNext {
                    Logger.d("FileOutputStream: $it")
                }.doOnError {
                    Logger.d("OnErrorReturn: $it")
                }.onErrorReturn {
                    Result.failure(Error.DuplicateFileError(it.toString()))
                }.defaultIfEmpty(Result.failure(Error.DuplicateFileError(DUPLICATE_FILE)))
    }

    @Suppress("UNCHECKED_CAST")
    fun saveAll(fileEntityList: List<FileEntity<Bitmap>>): Observable<Result<String>> {
        return Observable.fromIterable(fileEntityList).doOnNext { it ->
            Logger.d("Saving file: $it")
        }.flatMap { it ->
            save(it)
        }.toList().toObservable().map { it ->
            if (it.contains(Result.failure(Error.DuplicateFileError(DUPLICATE_FILE)))) {
                Result.failure(Error.DuplicateFileError(DUPLICATE_FILE))
            } else {
                Result.success(Unit) as Result<String>
            }
        }.doOnError { it ->
            Logger.d("Error occured: $it")
        }.onErrorReturn {
            Result.failure(Error.SavingFiles(SAVING_FILES_FAILED))
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

            it ->
            it.exists()

        }.doOnNext {
            Logger.d("Request exists in filesystem: ${it.exists()}")
            Logger.d("Deletion requested on: $it")
        }.map { it ->
            it.delete()
            Result.success(FILE_DELETED)
        }.onErrorReturn {
            Result.failure(Error.FileNotFoundError(it.toString()))
        }.defaultIfEmpty(
                Result.failure(Error.FileNotFoundError(FILE_NOT_FOUND))
        )
    }

    @Suppress("UNCHECKED_CAST")
    fun deleteAll(fileList: List<File>): Observable<Result<String>> {
        return Observable.fromIterable(fileList).doOnNext {
            Logger.d("Saving file: $it")
        }.flatMap { it ->
            delete(it)
        }.toList().toObservable().map { it ->
            if (it.contains(Result.failure(Error.FileNotFoundError(FILE_NOT_FOUND)))) {
                Result.failure(Error.DeletionFailed(DELETION_FAILED))
            } else {
                Result.success(FILES_DELETED)
            }
        }.doOnError {
            Logger.d("Error: $it")
        }.onErrorReturn { it ->
            Result.failure(Error.DeletionFailed(it.toString()))
        }
    }
}
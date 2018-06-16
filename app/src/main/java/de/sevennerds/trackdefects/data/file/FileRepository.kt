package de.sevennerds.trackdefects.data.file

import java.io.File
import android.graphics.Bitmap
import java.io.FileInputStream
import java.io.FileOutputStream
import android.graphics.BitmapFactory
import de.sevennerds.trackdefects.data.response.Result
import io.reactivex.Observable


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

    val BITMAP = "/projects"
    val APPLICATION_PATH = FileUtil.getExternalStorageDirectory()!!
    val WORKING_DIRECTORY = File(APPLICATION_PATH, BITMAP)
    val READABLE = FileUtil.isReadable()
    val WRITABLE = FileUtil.isWritable()
    val JPEG_QUALITY = 100
    val JPEG_FILE_EXTENSION = ".jpg"

    fun load(fileName: String): Observable<Result<Bitmap>> {
        if (!READABLE) {
            val file = File(WORKING_DIRECTORY, fileName)
            val input = FileInputStream(file)

            if (!file.exists()) return Observable.just(
                    Result.Failure("File does not exist.")
            )
            val bitmap = BitmapFactory.decodeStream(input)

            input.close()
            return Observable.just(Result.Success<Bitmap>(bitmap))
        }

    }

    fun save(genericFile: de.sevennerds.trackdefects.data.file.GenericFile<Bitmap>): Observable<Result<String>> {
        if (!WORKING_DIRECTORY.exists()) WORKING_DIRECTORY.mkdirs()
        if (WRITABLE) {
            val fileName = genericFile.name + JPEG_FILE_EXTENSION
            val newFile = File(WORKING_DIRECTORY, fileName)
            if (newFile.exists()) return Observable.just(Result.Failure("Filename conflict."))
            try {
                val output = FileOutputStream(newFile)
                genericFile.data.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, output)
                output.flush()
                output.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return Observable.just(
                Result.Failure("Directory not writable.")
        )
    }

    fun saveAll(genericFileList: List<GenericFile<Bitmap>>): Observable<Result<String>> {
        return ???
    }

    fun delete(file: File) {
        file.delete()
    }

}
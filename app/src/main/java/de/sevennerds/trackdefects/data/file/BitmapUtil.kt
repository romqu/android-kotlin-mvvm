package de.sevennerds.trackdefects.data.file

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Observable
import java.io.FileInputStream

object BitmapUtil {

    fun fileInputStreamToBitmap(fileInputStream: FileInputStream): Observable<Bitmap> {
        return Observable.just(fileInputStream).map { it ->
            BitmapFactory.decodeStream(it)
        }
    }

    /*
    fun bitmapToFileInputStream(bitmap: Bitmap): Observable<FileInputStream> {
        return Observable.just(bitmap).map {
            it ->
        }
    }
    */
}
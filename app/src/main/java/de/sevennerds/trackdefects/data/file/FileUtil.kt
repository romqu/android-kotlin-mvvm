package de.sevennerds.trackdefects.data.file

import android.os.Environment

object FileUtil {

    fun getExternalStorageDirectory(): String? {
        return Environment.getExternalStorageDirectory().toString()
    }

    fun isReadable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    fun isWritable(): Boolean {
        return Environment.getExternalStorageState() in setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }

}
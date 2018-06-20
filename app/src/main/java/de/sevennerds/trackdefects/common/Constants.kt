package de.sevennerds.trackdefects.common

import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.data.file.FileUtil

class Constants {
    companion object Database {
        // Database
        const val DATABASE_NAME = "trackdefects.db"
        const val DATABASE_VERSION = 1

        // Local File Storage
        val APPLICATION_EXTERNAL_ROOT = FileUtil.getExternalStorageDirectory() + "/TrackDefects"
        val FILES_PATH = APPLICATION_EXTERNAL_ROOT + "/projects"
    }
}
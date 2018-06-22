package de.sevennerds.trackdefects.common

import de.sevennerds.trackdefects.data.file.FileUtil

class Constants {
    companion object Database {

        /**
         * DATABASE
         */

        const val DATABASE_NAME = "trackdefects.db"
        const val DATABASE_VERSION = 1

        val APPLICATION_EXTERNAL_ROOT = FileUtil.getExternalStorageDirectory() + "/TrackDefects"
        val FILES_PATH = APPLICATION_EXTERNAL_ROOT + "/projects"


        /**
         * COMMON ERROR MESSAGE STRINGS
         */

        // File operations
        const val DIRECTORY_NOT_READABLE = "Directory not readable."
        const val DIRECTORY_NOT_WRITABLE = "Directory not writable."
        const val FILE_SAVED = "File successfully saved"
        const val FILE_DELETED = "File successfully deleted"
        const val FILES_DELETED = "Files successfully deleted"
        const val DUPLICATE_FILE = "Duplicate file."
        const val FILE_NOT_FOUND = "File not found."
        const val DELETION_FAILED = "Deletion failed."
        const val SAVING_FILES_FAILED = "Saving files failed."
        const val SAVING_FILE_FAILED = "Saving file failed."

        // Network failure
        const val NETWORK_NOT_AVAILABLE = "Network not available."
        const val NETWORK_REQUEST_FAILED = "Network request failed."
    }
}
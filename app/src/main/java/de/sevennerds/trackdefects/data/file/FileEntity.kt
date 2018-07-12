package de.sevennerds.trackdefects.data.file

data class FileEntity<T>(
        val name: String,
        val data: T,
        val path: String = ""
)
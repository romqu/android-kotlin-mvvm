package de.sevennerds.trackdefects.data.file

data class GenericFile<T>(
        val name: String,
        val data: T
)
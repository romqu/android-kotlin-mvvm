package de.sevennerds.trackdefects.presentation.model

data class FileModel<T>(val name: String,
                        val data: T,
                        val path: String = "") {
}
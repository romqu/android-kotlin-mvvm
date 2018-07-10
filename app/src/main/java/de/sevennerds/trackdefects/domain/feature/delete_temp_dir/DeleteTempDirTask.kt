package de.sevennerds.trackdefects.domain.feature.delete_temp_dir

import de.sevennerds.trackdefects.data.file.FileRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteTempDirTask @Inject constructor(private val fileRepository: FileRepository) {

    fun execute() =
        fileRepository
                .deleteTempDir()

}
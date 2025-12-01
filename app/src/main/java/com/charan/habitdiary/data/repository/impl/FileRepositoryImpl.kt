package com.charan.habitdiary.data.repository.impl

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.charan.habitdiary.data.repository.FileRepository
import com.charan.habitdiary.utils.ProcessState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class FileRepositoryImpl(
    private val context : Context
) : FileRepository {
    override fun saveImage(imageUri: Uri): Flow<ProcessState<String>> = flow {
        emit(ProcessState.Loading())

        try {
            val directory = File(context.filesDir, "habit_diary_images")
            if (!directory.exists()) {
                directory.mkdir()
            }

            val fileName = "IMG_${System.currentTimeMillis()}"
            val file = File(directory, fileName)
            context.contentResolver.openInputStream(imageUri).use { input ->
                if (input == null) {
                    emit(ProcessState.Error("Failed to read the image"))
                    return@flow
                }

                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }

            emit(ProcessState.Success(file.absolutePath))

        } catch (e: Exception) {
            e.printStackTrace()
            emit(ProcessState.Error(e.message ?: "An unexpected error occurred"))
        }
    }

    override fun createImageUri(): Uri {
        val file = File(
            context.cacheDir,
            "IMG_${System.currentTimeMillis()}.jpg"
        )

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    }
}
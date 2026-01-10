package com.charan.habitdiary.data.repository.impl

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.util.Log
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

    companion object {
        const val HABIT_DIARY_MEDIA_DIR = "habit_diary_media"
        // v0.1.0 was using this directory name
        const val HABIT_DIARY_IMAGES = "habit_diary_images"
    }
    override fun saveImagesToCache(imageUri: Uri) =
        saveMediaInternal(File(context.cacheDir, HABIT_DIARY_MEDIA_DIR), imageUri)

    override fun saveMedia(imageUri: Uri) =
        saveMediaInternal(File(context.filesDir, HABIT_DIARY_MEDIA_DIR), imageUri)



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

    override fun createVideoUri(): Uri {
        val file = File(
            context.cacheDir,
            "VID_${System.currentTimeMillis()}.mp4"
        )

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    }


    private fun saveMediaInternal(
        baseDir: File,
        sourceUri: Uri
    ): Flow<ProcessState<String>> = flow {
        emit(ProcessState.Loading())

        try {
            if (!baseDir.exists()) baseDir.mkdirs()
            val inputStream = if (sourceUri.scheme == "content") {
                context.contentResolver.openInputStream(sourceUri)
            } else {
                File(sourceUri.path ?: "").inputStream()
            }

            val mimeType = context.contentResolver.getType(sourceUri)
            val extension = when {
                mimeType?.startsWith("video/") == true || sourceUri.path?.endsWith(".mp4") == true -> ".mp4"
                mimeType?.startsWith("image/") == true || sourceUri.path?.endsWith(".jpg") == true -> ".jpg"
                else -> ".jpg"
            }

            val file = File(baseDir, "MEDIA_${System.currentTimeMillis()}$extension")

            inputStream?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            } ?: throw Exception("Could not open input stream")

            emit(ProcessState.Success(file.absolutePath))
        } catch (e: Exception) {
            emit(ProcessState.Error(e.message ?: "Unexpected error"))
        }
    }

    override fun clearCacheMedia() {
        try {
            println("Clearing cache media directory")
            val cacheDir = File(context.cacheDir, HABIT_DIARY_MEDIA_DIR)
            if (cacheDir.exists()) {
                cacheDir.deleteRecursively()
            }
        } catch (e: Exception) {
            Log.e("FileRepositoryImpl", "Error clearing cache media: ${e.message}")
        }
    }

    override fun saveMediaToDownloads(filePath: String): Flow<ProcessState<Boolean>> =flow{
        emit(ProcessState.Loading())
        try {
            println("Saving media to downloads....")
            val sourceFile = File(filePath)
            val downloadsDir = File(
                getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
                ), "HabitDiary"
            )
            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs()
            }
            val destFile = File(downloadsDir, sourceFile.name)
            sourceFile.copyTo(destFile, overwrite = true)
            println("Media saved to downloads: ${destFile.absolutePath}")
            emit(ProcessState.Success(true))
        } catch (e: Exception) {
            emit(ProcessState.Error(e.message ?: "Unexpected error"))
        }

    }

    override fun getMediaUri(filePath: String): Uri {
        val file = File(filePath)
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    }
}
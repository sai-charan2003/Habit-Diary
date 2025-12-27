package com.charan.habitdiary.data.repository

import android.net.Uri
import com.charan.habitdiary.utils.ProcessState
import kotlinx.coroutines.flow.Flow

interface FileRepository {

    fun saveImagesToCache(imageUri : Uri) : Flow<ProcessState<String>>

    fun saveMedia(imageUri : Uri) : Flow<ProcessState<String>>

    fun createImageUri() : Uri

    fun createVideoUri() : Uri

    fun clearCacheMedia()

    fun saveMediaToDownloads(filePath : String) : Flow<ProcessState<Boolean>>

    fun getMediaUri(filePath : String) : Uri
}
package com.charan.habitdiary.data.repository

import android.net.Uri
import com.charan.habitdiary.utils.ProcessState
import kotlinx.coroutines.flow.Flow

interface FileRepository {

    fun saveImage(imageUri : Uri) : Flow<ProcessState<String>>

    fun createImageUri() : Uri
}
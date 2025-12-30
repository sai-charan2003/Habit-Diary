package com.charan.habitdiary.data.repository

import android.net.Uri
import com.charan.habitdiary.utils.ProcessState
import kotlinx.coroutines.flow.Flow

interface BackupRepository {

    suspend fun backupData(uri : Uri?): Flow<ProcessState<Boolean>>

    suspend fun importData(uri : Uri?) : Flow<ProcessState<Boolean>>
    val fileName : String
}
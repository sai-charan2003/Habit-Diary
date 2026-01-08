package com.charan.habitdiary.data.repository.impl

import android.content.Context
import android.net.Uri
import android.util.Log
import com.charan.habitdiary.BuildConfig
import com.charan.habitdiary.data.local.entity.DailyLogEntity
import com.charan.habitdiary.data.local.entity.DailyLogMediaEntity
import com.charan.habitdiary.data.local.entity.HabitEntity
import com.charan.habitdiary.data.local.model.BackupMetaData
import com.charan.habitdiary.data.repository.BackupRepository
import com.charan.habitdiary.data.repository.FileRepository
import com.charan.habitdiary.data.repository.HabitLocalRepository
import com.charan.habitdiary.data.repository.impl.FileRepositoryImpl.Companion.HABIT_DIARY_IMAGES
import com.charan.habitdiary.data.repository.impl.FileRepositoryImpl.Companion.HABIT_DIARY_MEDIA_DIR
import com.charan.habitdiary.notification.NotificationScheduler
import com.charan.habitdiary.utils.ProcessState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json
import okhttp3.Dispatcher
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.util.UUID
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import kotlin.random.Random


class BackupRepositoryImpl(
    private val context : Context,
    private val habitLocalRepository: HabitLocalRepository,
    private val notificationScheduler: NotificationScheduler
) : BackupRepository{
    companion object{
        const val FILE_TYPE = "application/zip"

        private const val DATA_DIR = "data/"

        const val HABIT_FILE = "${DATA_DIR}habits.json"
        const val DAILY_LOG_FILE = "${DATA_DIR}daily_logs.json"
        const val MEDIA_FILE = "${DATA_DIR}media.json"

        const val META_FILE = "meta.json"

        const val HABIT_MEDIA_DIR = "habitMedia/"
        const val HABIT_IMAGES_DIR = "habitImages/"
    }
    override suspend fun backupData(uri: Uri?): Flow<ProcessState<Boolean>> = flow{
        if(uri == null){
            emit(ProcessState.Error("No File Found"))
            return@flow
        }
        emit(ProcessState.Loading())
        try {
            val habits = habitLocalRepository.getAllHabits()
            val dailyLogs = habitLocalRepository.getAllDailyLogs()
            val media = habitLocalRepository.getAllMedia()
            val metaData = BackupMetaData(
                versionCode = BuildConfig.VERSION_CODE.toString(),
                appVersion = BuildConfig.VERSION_NAME,
                createdAt = System.currentTimeMillis()
            )

            val habitJson = Json.encodeToString(habits)
            val dailyLogJson = Json.encodeToString(dailyLogs)
            val mediaJson = Json.encodeToString(media)
            val metaJson = Json.encodeToString(metaData)



            val outputStream = context.contentResolver.openOutputStream(uri)

            ZipOutputStream(BufferedOutputStream(outputStream)).use { zip->

                zip.putNextEntry(ZipEntry(META_FILE))
                zip.write(metaJson.toByteArray())
                zip.closeEntry()


                zip.putNextEntry(ZipEntry(HABIT_FILE))
                zip.write(habitJson.toByteArray())
                zip.closeEntry()

                zip.putNextEntry(ZipEntry(DAILY_LOG_FILE))
                zip.write(dailyLogJson.toByteArray())
                zip.closeEntry()

                zip.putNextEntry(ZipEntry(MEDIA_FILE))
                zip.write(mediaJson.toByteArray())
                zip.closeEntry()

                val mediaFiles = File(context.filesDir,HABIT_DIARY_MEDIA_DIR)
                val oldFiles = File(context.filesDir,HABIT_DIARY_IMAGES)

                if(mediaFiles.exists()){
                    mediaFiles.listFiles()?.forEach { file ->
                        zip.putNextEntry(ZipEntry("$HABIT_MEDIA_DIR${file.name}"))
                        file.inputStream().use { input ->
                            input.copyTo(zip)
                        }
                        zip.closeEntry()
                    }
                }

                if(oldFiles.exists()){
                    oldFiles.listFiles()?.forEach { file ->
                        zip.putNextEntry(ZipEntry("$HABIT_IMAGES_DIR${file.name}"))
                        file.inputStream().use { input ->
                            input.copyTo(zip)
                        }
                        zip.closeEntry()
                    }
                }

            }
            emit(ProcessState.Success(true))

        } catch (e: Exception) {
            emit(ProcessState.Error(e.message ?: "An error occurred"))
        }

    }

    override suspend fun importData(uri: Uri?): Flow<ProcessState<Boolean>> = flow {
        if (uri == null) {
            emit(ProcessState.Error("No File Found"))
            return@flow
        }
        emit(ProcessState.Loading())
        var importedHabits: List<HabitEntity> = emptyList()
        var importedDailyLogs: List<DailyLogEntity> = emptyList()
        var importedMediaEntities: List<DailyLogMediaEntity> = emptyList()

        val fileNameMapping = mutableMapOf<String, String>()

        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            ZipInputStream(BufferedInputStream(inputStream)).use { zip ->
                var entry = zip.nextEntry

                while (entry != null) {
                    val entryName = entry.name

                    when {
                        entryName == HABIT_FILE -> {
                            val json = zip.readBytes().toString(Charsets.UTF_8)
                            if (json.isNotEmpty()) {
                                importedHabits = Json.decodeFromString(json)
                            }
                        }
                        entryName == DAILY_LOG_FILE -> {
                            val json = zip.readBytes().toString(Charsets.UTF_8)
                            if (json.isNotEmpty()) {
                                importedDailyLogs = Json.decodeFromString(json)
                            }
                        }
                        entryName == MEDIA_FILE -> {
                            val json = zip.readBytes().toString(Charsets.UTF_8)
                            if (json.isNotEmpty()) {
                                importedMediaEntities = Json.decodeFromString(json)
                            }
                        }


                        entryName.startsWith(HABIT_MEDIA_DIR) || entryName.startsWith(HABIT_IMAGES_DIR) -> {
                            val targetDir = File(context.filesDir, HABIT_DIARY_MEDIA_DIR)
                            if (!targetDir.exists()) targetDir.mkdirs()
                            val originalFileName = File(entryName).name

                            val extension = originalFileName.substringAfterLast('.', ".jpg")
                            val newFileName = "MEDIA_${System.currentTimeMillis()}_${UUID.randomUUID()}.$extension"

                            val newFile = File(targetDir, newFileName)

                            BufferedOutputStream(newFile.outputStream()).use { output ->
                                zip.copyTo(output)
                            }

                            fileNameMapping[originalFileName] = newFile.absolutePath
                        }
                    }
                    zip.closeEntry()
                    entry = zip.nextEntry
                }
            }

            if (importedHabits.isNotEmpty()) {
                val insertHabits = importedHabits.map { it.copy(id = 0) }
                val ids = habitLocalRepository.insertHabits(insertHabits)

                insertHabits.forEachIndexed { index, habit ->
                    val newId = ids[index]
                    if (habit.isReminderEnabled) {
                        notificationScheduler.scheduleReminder(
                            habitId = newId,
                            time = habit.habitReminder,
                            frequency = habit.habitFrequency,
                            isReminderEnabled = true
                        )
                    }
                }


            }

            val newDailyLogIdMap = mutableMapOf<Long, Long>()
            if (importedDailyLogs.isNotEmpty()) {
                val insertDailyLogs = importedDailyLogs.map { it.copy(id = 0) }
                val newIds = habitLocalRepository.insertDailyLogs(insertDailyLogs)

                importedDailyLogs.forEachIndexed { index, oldEntity ->
                    newDailyLogIdMap[oldEntity.id] = newIds[index]
                }
            }
            if (importedMediaEntities.isNotEmpty()) {
                val finalMediaEntities = importedMediaEntities.mapNotNull { mediaEntity ->
                    val newLogId = newDailyLogIdMap[mediaEntity.dailyLogId] ?: return@mapNotNull null
                    val oldFileName = File(mediaEntity.mediaPath).name
                    val newPath = fileNameMapping[oldFileName]
                    if (newPath != null) {
                        mediaEntity.copy(
                            id = 0,
                            dailyLogId = newLogId,
                            mediaPath = newPath
                        )
                    } else {
                        null
                    }
                }

                habitLocalRepository.upsetDailyLogMediaEntities(finalMediaEntities)
            }

            emit(ProcessState.Success(true))

        } catch (e: Exception) {
            e.printStackTrace()
            emit(ProcessState.Error(e.message ?: "An error occurred"))
        }
    }

    override val fileName: String
        get() {
            val appName = context.applicationInfo.loadLabel(context.packageManager)
            return "${appName}_Backup_${System.currentTimeMillis()}.zip"
        }
}
package com.charan.habitdiary.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1,2){
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `daily_log_media_entity` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `dailyLogId` INTEGER NOT NULL,
                `mediaPath` TEXT NOT NULL,
                `isDeleted` INTEGER NOT NULL DEFAULT 0,
                FOREIGN KEY(`dailyLogId`) REFERENCES `daily_log_entity`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE INDEX IF NOT EXISTS `index_daily_log_media_entity_dailyLogId` ON `daily_log_media_entity` (`dailyLogId`)
            """.trimIndent()
        )

        db.execSQL(
            """
                INSERT INTO daily_log_media_entity (dailyLogId, mediaPath)
            SELECT id, imagePath
            FROM daily_log_entity
            WHERE imagePath IS NOT NULL AND imagePath != ''
            """.trimIndent()
        )
    }
}
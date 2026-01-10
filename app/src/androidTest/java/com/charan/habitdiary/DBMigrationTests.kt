package com.charan.habitdiary

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.charan.habitdiary.data.local.AppDatabase
import com.charan.habitdiary.data.local.MIGRATION_1_2
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DBMigrationTests {
    private val TEST_DB = "app-database-test"
    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java,
        listOf(),
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrateAll() {
        helper.createDatabase(TEST_DB, 1).apply {
            close()
        }
        Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            AppDatabase::class.java,
            TEST_DB
        ).addMigrations(
            MIGRATION_1_2
        ).build().apply {
            openHelper.writableDatabase.close()
        }
    }


    @Test
    fun migrate_1_to_2_movesImagePathIntoMediaTable() {

        val dbV1 = helper.createDatabase(TEST_DB, 1)
        dbV1.execSQL(
            """
    INSERT INTO daily_log_entity 
        (id, logNote, imagePath, createdAt, isDeleted, habitId)
    VALUES
        (1, 'note 1', '/storage/img1.jpg', '2025-01-01T10:00', 0, NULL),
        (2, 'note 2', '', '2025-01-01T11:00', 0, NULL),
        (3, 'note 3', '', '2025-01-01T12:00', 0, NULL)
    """.trimIndent()
        )


        dbV1.close()

        val dbV2 = helper.runMigrationsAndValidate(
            TEST_DB,
            2,
            true,
            MIGRATION_1_2
        )

        val cursor = dbV2.query(
            """
        SELECT dailyLogId, mediaPath, isDeleted
        FROM daily_log_media_entity
        """
        )

        assert(cursor.count == 1)
        cursor.moveToFirst()

        assert(cursor.getInt(0) == 1)
        assert(cursor.getString(1) == "/storage/img1.jpg")
        assert(cursor.getInt(2) == 0)

        cursor.close()
    }

}
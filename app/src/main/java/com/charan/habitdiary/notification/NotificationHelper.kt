package com.charan.habitdiary.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.Context
import android.os.Build

class NotificationHelper(private val context: Context) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    init {
        createHabitReminderNotificationChannel()
    }

    companion object {
        private const val HABIT_REMINDER_CHANNEL_ID = "habit_reminder_channel"
        private const val HABIT_REMINDER_CHANNEL_NAME = "Habit Reminders"
    }

    fun createHabitReminderNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                HABIT_REMINDER_CHANNEL_ID,
                HABIT_REMINDER_CHANNEL_NAME,
                IMPORTANCE_HIGH
            ).apply {
                description = "Habit Reminder notifications"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(title: String, message: String) {

        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, HABIT_REMINDER_CHANNEL_ID)
        } else {
            Notification.Builder(context)
        }

        val notification = builder
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}

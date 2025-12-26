package com.charan.habitdiary.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.charan.habitdiary.R

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
        val channel = NotificationChannel(
            HABIT_REMINDER_CHANNEL_ID,
            HABIT_REMINDER_CHANNEL_NAME,
            IMPORTANCE_HIGH
        ).apply {
            description = "Habit Reminder notifications"
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun showNotification(
        title: String,
        message: String,
        habitId : Int
    ) {
        val markAsDoneIntent = Intent(context, NotificationReceiver::class.java).apply {
            action = IntentActions.MARK_AS_DONE.name
            putExtra("habitId", habitId)
        }
        val actionIntent = PendingIntent.getBroadcast(
            context,
            habitId,
            markAsDoneIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder =
            Notification.Builder(context, HABIT_REMINDER_CHANNEL_ID)

        val notification = builder
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.notification_icon)
            .setAutoCancel(true)
            .addAction(
                Notification.Action.Builder(
                    null,
                    "Mark as Done",
                    actionIntent
                ).build()
            )
            .build()

        notificationManager.notify(habitId, notification)
    }

    fun cancelNotification(habitId : Int){
        notificationManager.cancel(habitId)
    }
}

package com.charan.habitdiary.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.charan.habitdiary.data.repository.HabitLocalRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {
    @Inject lateinit var notificationHelper: NotificationHelper
    @Inject lateinit var habitLocalRepository: HabitLocalRepository

    @Inject lateinit var notificationScheduler: NotificationScheduler
    override fun onReceive(context: Context?, intent: Intent?) {
        val pendingResult = goAsync()
        val appContext = context?.applicationContext

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val habitId = intent?.getIntExtra("habitId", -1) ?: -1
                if (habitId != -1 && appContext != null) {
                    val habit = habitLocalRepository.getHabitWithId(habitId)
                    val habitLog = habitLocalRepository.getLoggedHabitFromIdForRange(habitId)
                    if(habitLog == null){
                        notificationHelper.showNotification(
                            title = "Habit Reminder",
                            message = "It's time for your habit: ${habit.habitName}"
                        )
                    }
                    notificationScheduler.scheduleReminder(
                        habitId = habit.id,
                        time = habit.habitReminder,
                        frequency = habit.habitFrequency,
                        isReminderEnabled = habit.isReminderEnabled
                    )
                }
            } finally {
                pendingResult.finish()
            }
        }
    }


}
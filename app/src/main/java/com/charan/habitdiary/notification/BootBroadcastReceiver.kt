package com.charan.habitdiary.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.charan.habitdiary.data.repository.HabitLocalRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootBroadcastReceiver : BroadcastReceiver() {
    @Inject lateinit var habitLocalRepository: HabitLocalRepository
    @Inject lateinit var notificationScheduler: NotificationScheduler
    override fun onReceive(context: Context?, intent: Intent?) {
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch{
            if(intent?.action == Intent.ACTION_BOOT_COMPLETED){
                try {
                    Log.d("TAG", "onReceive: hi")
                    val habits = habitLocalRepository.getAllHabits()
                    for (habit in habits) {
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
            } else {
                pendingResult.finish()
            }

        }



    }
}
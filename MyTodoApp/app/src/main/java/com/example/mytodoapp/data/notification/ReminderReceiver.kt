package com.example.mytodoapp.data.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getLongExtra("task_id", -1L)
        val taskTitle = intent.getStringExtra("task_title") ?: "Task Reminder"
        val taskDescription = intent.getStringExtra("task_description") ?: ""

        if (taskId != -1L) {
            NotificationHelper(context).showTaskReminderNotification(
                taskId = taskId,
                taskTitle = taskTitle,
                taskDescription = taskDescription
            )
        }
    }
}
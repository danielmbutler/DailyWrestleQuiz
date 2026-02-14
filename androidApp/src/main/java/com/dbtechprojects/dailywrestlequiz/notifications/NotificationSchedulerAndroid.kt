package com.dbtechprojects.dailywrestlequiz.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar

class NotificationSchedulerAndroid(private val context: Context) : NotificationScheduler {

    private fun buildPendingIntent(): PendingIntent {
        val intent = Intent(context, com.dbtechprojects.dailywrestlequiz.notifications.MiddayReminderReceiver::class.java).apply {
            action = com.dbtechprojects.dailywrestlequiz.notifications.MiddayReminderReceiver.ACTION_MIDDAY_ALARM
        }
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getBroadcast(context, 0, intent, flags)
    }

    override fun scheduleMiddayReminder() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        // if midday already passed today, schedule for tomorrow
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        val pending = buildPendingIntent()
        // schedule an exact alarm at noon (allow while idle for doze)
        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pending
            )
        } catch (_: Exception) {
            // fallback to inexact repeating
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pending
            )
        }
    }

    override fun cancelMiddayReminder() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pending = buildPendingIntent()
        alarmManager.cancel(pending)
    }

    override fun postImmediateReminder() {
        val receiverIntent = Intent(context, com.dbtechprojects.dailywrestlequiz.notifications.MiddayReminderReceiver::class.java).apply {
            action = com.dbtechprojects.dailywrestlequiz.notifications.MiddayReminderReceiver.ACTION_MIDDAY_ALARM
        }
        // Fire the receiver directly
        context.sendBroadcast(receiverIntent)
    }
}

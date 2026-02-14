package com.dbtechprojects.dailywrestlequiz.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            // Re-schedule the midday alarm after boot — use NotificationSchedulerAndroid directly
            val scheduler = NotificationSchedulerAndroid(context.applicationContext)
            scheduler.scheduleMiddayReminder()
        }
    }
}

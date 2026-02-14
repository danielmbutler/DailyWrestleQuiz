package com.dbtechprojects.dailywrestlequiz.notifications

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.dbtechprojects.dailywrestlequiz.android.R

class MiddayReminderReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_MIDDAY_ALARM = "com.dbtechprojects.dailywrestlequiz.MIDDAY_ALARM"
        private const val CHANNEL_ID = "daily_streak_channel"
        private const val CHANNEL_NAME = "Daily Streak Reminders"
        private const val NOTIFICATION_ID = 1001
    }

    override fun onReceive(context: Context, intent: Intent?) {
        // Check action to avoid reacting to unrelated broadcasts
        if (intent?.action != null && intent.action != ACTION_MIDDAY_ALARM) return

        // On Android 13+ ensure we have the POST_NOTIFICATIONS permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val has = ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            if (!has) return
        }

        // If notifications are disabled for the app, bail out
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) return

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            nm.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.app_icon)
            .setContentTitle("Don't lose your streak")
            .setContentText("It's past 12:00 — try today's quiz to keep your streak going.")
            .setAutoCancel(true)

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    }
}

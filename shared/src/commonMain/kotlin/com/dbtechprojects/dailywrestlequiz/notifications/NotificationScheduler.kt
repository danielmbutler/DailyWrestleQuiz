package com.dbtechprojects.dailywrestlequiz.notifications

interface NotificationScheduler {
    fun scheduleMiddayReminder()
    fun cancelMiddayReminder()
    fun postImmediateReminder()
}

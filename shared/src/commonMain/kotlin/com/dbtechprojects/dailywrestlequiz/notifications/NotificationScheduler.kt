package com.dbtechprojects.dailywrestlequiz.notifications

interface NotificationScheduler {
    fun scheduleMiddayReminder()
    fun cancelMiddayReminder()
    fun postImmediateReminder()
}

object StubNotificationScheduler : NotificationScheduler {
    override fun scheduleMiddayReminder() {
        // No-op for non-Android platforms
    }

    override fun cancelMiddayReminder() {
        // No-op for non-Android platforms
    }

    override fun postImmediateReminder() {
        // No-op for non-Android platforms
    }
}

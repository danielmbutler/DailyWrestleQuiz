package com.dbtechprojects.dailywrestlequiz.data.usecase

import com.dbtechprojects.dailywrestlequiz.data.data.persistence.database.daos.QuestionDao
import com.dbtechprojects.dailywrestlequiz.data.data.persistence.database.daos.ScoreDao
import com.dbtechprojects.dailywrestlequiz.data.data.persistence.database.daos.SettingsDao
import com.dbtechprojects.dailywrestlequiz.data.model.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


interface SettingsUseCase{
    fun getStreak() : Flow<Int>

    fun canAccessStreakMode(currentStreakDate: String) : Boolean

    suspend fun getSettings(): Flow<Settings?>

    suspend fun clearData()
}
class SettingsUseCaseImpl(
    private val settingsDao: SettingsDao,
    private val timerUtils: TimerUtils,
    private val notificationScheduler: com.dbtechprojects.dailywrestlequiz.notifications.NotificationScheduler,
    private val scoreDao: ScoreDao,
    private val questionDao: QuestionDao
) : SettingsUseCase{

    override fun getStreak(): Flow<Int> {
        return settingsDao.getSettingsFlow().map { settings -> settings?.streak ?: 0}
    }

    override fun canAccessStreakMode(currentStreakDate: String): Boolean {
        val nowLocal = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())

        val today = nowLocal.date // extract LocalDate
        val hour = nowLocal.hour

        println("Streak Today: $today")

        val streakDt = timerUtils.getLocalDateTimeFromString(currentStreakDate)

        // If we don't have a streak date stored, user hasn't attempted — schedule or post reminder.
        if (streakDt == null) {
            if (hour < 12) {
                notificationScheduler.scheduleMiddayReminder()
            } else {
                // Past midday and no attempt today — post immediately
                notificationScheduler.postImmediateReminder()
            }
            return true
        }

        val isToday = streakDt.date == today

        // If user has not attempted today, ensure a reminder is scheduled (or posted if past midday)
        if (!isToday) {
            if (hour < 12) {
                notificationScheduler.scheduleMiddayReminder()
            } else {
                notificationScheduler.postImmediateReminder()
            }
        } else {
            // user already attempted today -> cancel any scheduled reminder
            notificationScheduler.cancelMiddayReminder()
        }

        return !isToday
    }

    override suspend fun getSettings(): Flow<Settings?> {
        return settingsDao.getSettingsFlow()
    }

    override suspend fun clearData() {
        // Clear settings, scores and reset question counters
        settingsDao.clearAllSettings()
        scoreDao.clearAllScores()
        scoreDao.clearAllTimeTrialScores()
        questionDao.resetAllTimesAnswered()
    }

}
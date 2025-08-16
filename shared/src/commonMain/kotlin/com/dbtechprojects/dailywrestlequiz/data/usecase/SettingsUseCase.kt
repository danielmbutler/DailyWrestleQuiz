package com.dbtechprojects.dailywrestlequiz.data.usecase

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
}
class SettingsUseCaseImpl(
    private val settingsDao: SettingsDao,
    private val timerUtils: TimerUtils) : SettingsUseCase{

    override fun getStreak(): Flow<Int> {
        return settingsDao.getSettingsFlow().map { settings -> settings?.streak ?: 0}
    }

    override fun canAccessStreakMode(currentStreakDate: String): Boolean {
        val today = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date // extract LocalDate

        println("Streak Today: $today")

        val streakDt = timerUtils.getLocalDateTimeFromString(currentStreakDate) ?: return true

        println("Streak date: $streakDt")
        println("is today ${streakDt.date != today}")
        return streakDt.date != today
    }

    override suspend fun getSettings(): Flow<Settings?> {
        return settingsDao.getSettingsFlow()
    }

}
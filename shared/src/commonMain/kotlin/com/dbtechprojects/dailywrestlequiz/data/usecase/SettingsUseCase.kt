package com.dbtechprojects.dailywrestlequiz.data.usecase

import com.dbtechprojects.dailywrestlequiz.data.data.persistence.database.daos.SettingsDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


interface SettingsUseCase{
    fun getStreak() : Flow<Int>
}
class SettingsUseCaseImpl(private val settingsDao: SettingsDao) : SettingsUseCase{

    override fun getStreak(): Flow<Int> {
        return settingsDao.getSettingsFlow().map { settings -> settings?.streak ?: 0}
    }
}
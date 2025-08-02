package com.dbtechprojects.dailywrestlequiz.data.data.persistence.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dbtechprojects.dailywrestlequiz.data.model.Settings
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {

    @Query("SELECT * FROM settings LIMIT 1")
    fun getSettingsFlow(): Flow<Settings?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: Settings)

    @Query("UPDATE Settings SET streak = streak + 1 WHERE id = 1")
    suspend fun increaseStreak()

    @Query("UPDATE Settings SET streak = 0 WHERE id = 1")
    suspend fun endStreak()
}
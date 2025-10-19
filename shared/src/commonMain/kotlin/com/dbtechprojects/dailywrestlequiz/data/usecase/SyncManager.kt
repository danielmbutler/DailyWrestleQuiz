package com.dbtechprojects.dailywrestlequiz.data.usecase

import com.dbtechprojects.dailywrestlequiz.data.data.persistence.database.daos.QuestionDao
import com.dbtechprojects.dailywrestlequiz.data.data.persistence.database.daos.SettingsDao
import com.dbtechprojects.dailywrestlequiz.data.data.persistence.file.decryptFileToJson
import com.dbtechprojects.dailywrestlequiz.data.data.persistence.file.getKey
import com.dbtechprojects.dailywrestlequiz.data.model.Question
import com.dbtechprojects.dailywrestlequiz.data.model.Settings
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.take

class SyncManager(
    private val questionDao: QuestionDao,
    private val settingsDao: SettingsDao
) {
    private val key = getKey().also {
        println("key: " + it)
    } // get from firebase


    suspend fun runSync(){
        // decrypt file
        syncLocalQuestions()
        setupAppSettings()
    }

    private suspend fun setupAppSettings(){
        settingsDao.getSettingsFlow().firstOrNull{ settings ->
            if (settings == null){
                settingsDao.saveSettings(Settings.initial)
            }
            true
        }
    }

    private suspend fun syncLocalQuestions(){
        questionDao.count().let {
            if (it > 50){
                println("sync not needed count is $it")
                return
            }
        }
        val decryptedJson = decryptFileToJson("questions", key)
        if (decryptedJson != null) {
            println(" sync Decrypted JSON: true")
            Question.fromJson(decryptedJson)?.let {
                questionDao.insert(it)
            }
            println("sync question count " + questionDao.count())
            // parse JSON here
        } else {
            println("Failed to decrypt file")
        }
    }
}
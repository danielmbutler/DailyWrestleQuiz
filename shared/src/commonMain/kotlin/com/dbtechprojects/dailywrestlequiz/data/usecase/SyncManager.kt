package com.dbtechprojects.dailywrestlequiz.data.usecase

import com.dbtechprojects.dailywrestlequiz.data.data.persistence.database.daos.QuestionDao
import com.dbtechprojects.dailywrestlequiz.data.data.persistence.file.decryptFileToJson
import com.dbtechprojects.dailywrestlequiz.data.model.Question

class SyncManager(
    private val questionDao: QuestionDao
) {
    private val key = "kjasdhfjshfkjsdhfksdjhfksdjfhsdk" // get from firebase


    suspend fun runSync(){
        // decrypt file
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
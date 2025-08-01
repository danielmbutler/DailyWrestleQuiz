package com.dbtechprojects.dailywrestlequiz.data.data.persistence.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dbtechprojects.dailywrestlequiz.data.model.Question

@Dao
interface QuestionDao {

    @Insert
    suspend fun insert(item: Question)

    @Insert
    suspend fun insert(items: List<Question>)

    @Query("SELECT count(*) FROM Question")
    suspend fun count(): Int

    @Query("UPDATE Question SET timesAnswered = timesAnswered + 1 WHERE question_id = :questionId")
    suspend fun updateTimesAnswered(questionId: Int)
}
package com.dbtechprojects.dailywrestlequiz.data.data.persistence.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dbtechprojects.dailywrestlequiz.data.model.Score

@Dao
interface ScoreDao {

    @Query("SELECT * FROM Score WHERE quizId = :quizId ORDER BY score DESC LIMIT 1")
    suspend fun getScore(quizId: Int): Score?

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateScore(score: Score)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(score: Score)


}
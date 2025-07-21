package com.dbtechprojects.dailywrestlequiz.android.ui

import androidx.lifecycle.ViewModel
import com.dbtechprojects.dailywrestlequiz.data.model.Quiz

class NavViewModel : ViewModel() {

    private var selectedQuiz: Quiz? = null

    fun setSelectedQuiz(quiz: Quiz) {
        selectedQuiz = quiz
    }

    fun getSelectedQuiz(): Quiz? {
        return selectedQuiz
    }
}
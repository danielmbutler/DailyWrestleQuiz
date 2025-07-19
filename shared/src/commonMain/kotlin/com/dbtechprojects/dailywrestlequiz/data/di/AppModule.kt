package com.dbtechprojects.dailywrestlequiz.data.di

import com.dbtechprojects.dailywrestlequiz.data.model.Quiz
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuestionsUseCase
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuestionsUseCaseImpl
import com.dbtechprojects.dailywrestlequiz.data.usecase.TimerUtils
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuestionViewModel
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuestionViewModelImpl
import org.koin.dsl.module

object AppModule {
    val appModule = module {
        single<QuestionsUseCase> { QuestionsUseCaseImpl() }
        single { TimerUtils() }
        factory { (quiz: Quiz) -> QuestionViewModelImpl(get(), quiz, get()) }
    }
}
package com.dbtechprojects.dailywrestlequiz.data.di

import com.dbtechprojects.dailywrestlequiz.data.usecase.QuestionsUseCase
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuestionsUseCaseImpl
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.HomeViewModel
import org.koin.dsl.module

object AppModule {
    val appModule = module {
        single<QuestionsUseCase> { QuestionsUseCaseImpl() }
        single { HomeViewModel(get()) }
    }
}
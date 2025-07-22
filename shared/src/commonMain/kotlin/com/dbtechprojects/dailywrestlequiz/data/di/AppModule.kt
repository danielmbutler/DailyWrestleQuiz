package com.dbtechprojects.dailywrestlequiz.data.di

import com.dbtechprojects.dailywrestlequiz.data.model.Quiz
import com.dbtechprojects.dailywrestlequiz.data.model.TimeTrial
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuestionsUseCase
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuestionsUseCaseImpl
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuizUseCase
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuizUseCaseImpl
import com.dbtechprojects.dailywrestlequiz.data.usecase.TimeTrialUseCase
import com.dbtechprojects.dailywrestlequiz.data.usecase.TimeTrialUseCaseImpl
import com.dbtechprojects.dailywrestlequiz.data.usecase.TimerUtils
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuestionViewModelImpl
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuizViewModelImpl
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.TimeTrialGameViewModelImpl
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.TimeTrialListViewModelImpl
import org.koin.dsl.module

object AppModule {
    val appModule = module {
        single<QuestionsUseCase> { QuestionsUseCaseImpl() }
        single<TimeTrialUseCase> { TimeTrialUseCaseImpl() }
        single<QuizUseCase> { QuizUseCaseImpl() }
        single { TimerUtils() }

        factory { (args: ArgPersistence<Int>) -> QuestionViewModelImpl(get(), get(), args, get()) }

        single { QuizViewModelImpl(get()) }

        single { TimeTrialListViewModelImpl(get()) }

        factory { (args: ArgPersistence<Int>) ->
            TimeTrialGameViewModelImpl(get(), get(), args)
        }
}
}
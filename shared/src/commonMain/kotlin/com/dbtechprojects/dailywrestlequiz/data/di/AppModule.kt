package com.dbtechprojects.dailywrestlequiz.data.di

import androidx.room.RoomDatabase
import com.dbtechprojects.dailywrestlequiz.data.data.persistence.database.AppDatabase
import com.dbtechprojects.dailywrestlequiz.data.data.persistence.database.daos.QuestionDao
import com.dbtechprojects.dailywrestlequiz.data.data.persistence.database.getRoomDatabase
import com.dbtechprojects.dailywrestlequiz.data.model.Quiz
import com.dbtechprojects.dailywrestlequiz.data.model.TimeTrial
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuestionsUseCase
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuestionsUseCaseImpl
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuizUseCase
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuizUseCaseImpl
import com.dbtechprojects.dailywrestlequiz.data.usecase.SyncManager
import com.dbtechprojects.dailywrestlequiz.data.usecase.TimeTrialUseCase
import com.dbtechprojects.dailywrestlequiz.data.usecase.TimeTrialUseCaseImpl
import com.dbtechprojects.dailywrestlequiz.data.usecase.TimerUtils
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.HomeViewModel
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.HomeViewModelImpl
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuestionViewModelImpl
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuizViewModelImpl
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.TimeTrialGameViewModelImpl
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.TimeTrialListViewModelImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

object AppModule {

    val databaseModule = module {
        // Provide the database instance (assume platform code provides builder)
        single<AppDatabase> {
            // For Android, platform code will provide RoomDatabase.Builder<AppDatabase>
            val builder: RoomDatabase.Builder<AppDatabase> = get(named("dbBuilder"))
            getRoomDatabase(builder)
        }

        // Provide the DAO from database
        single<QuestionDao> { get<AppDatabase>().getDao() }
    }
    val appModule = module {
        single { SyncManager(get()) }  // Koin injects QuestionDao automatically here
        single<QuestionsUseCase> { QuestionsUseCaseImpl() }
        single<TimeTrialUseCase> { TimeTrialUseCaseImpl() }
        single<QuizUseCase> { QuizUseCaseImpl() }
        single<HomeViewModel> { HomeViewModelImpl(get()) }
        single { TimerUtils() }

        factory { (args: ArgPersistence<Int>) -> QuestionViewModelImpl(get(), get(), args, get()) }

        single { QuizViewModelImpl(get()) }

        single { TimeTrialListViewModelImpl(get()) }

        factory { (args: ArgPersistence<Int>) ->
            TimeTrialGameViewModelImpl(get(), get(), args)
        }
    }
}
package com.dbtechprojects.dailywrestlequiz

import android.app.Application
import com.dbtechprojects.dailywrestlequiz.data.data.persistence.database.androidModule
import com.dbtechprojects.dailywrestlequiz.data.di.AppModule.appModule
import com.dbtechprojects.dailywrestlequiz.data.di.AppModule.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class DailyWrestlingTrivia : Application() {
    override fun onCreate() {
        super.onCreate()
        initAppContext(this.applicationContext)
        startKoin {
            androidLogger()
            androidContext(this@DailyWrestlingTrivia)
            modules(listOf(androidModule, databaseModule, appModule)) // your other modules
        }
    }
}
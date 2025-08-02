package com.dbtechprojects.dailywrestlequiz.data.data.persistence.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.core.qualifier.named
import org.koin.dsl.module


fun androidDatabaseBuilder(ctx: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = ctx.applicationContext
    return Room.databaseBuilder(
        appContext,
        AppDatabase::class.java,
        "my_room.db"
    )
}

val androidModule = module {
    single(named("dbBuilder")) { androidDatabaseBuilder(get()) }
}
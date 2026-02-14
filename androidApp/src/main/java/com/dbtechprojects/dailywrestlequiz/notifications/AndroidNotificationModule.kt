package com.dbtechprojects.dailywrestlequiz.notifications

import org.koin.dsl.module

val notificationModule = module {
    single<NotificationScheduler> { NotificationSchedulerAndroid(get()) }
}

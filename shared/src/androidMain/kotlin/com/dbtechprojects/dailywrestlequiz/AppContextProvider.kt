package com.dbtechprojects.dailywrestlequiz

import android.content.Context

lateinit var appContext: Context
    private set

fun initAppContext(context: Context) {
    appContext = context.applicationContext
}
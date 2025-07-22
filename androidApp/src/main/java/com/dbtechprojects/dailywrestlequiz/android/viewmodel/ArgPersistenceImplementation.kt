package com.dbtechprojects.dailywrestlequiz.android.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.dbtechprojects.dailywrestlequiz.data.di.ArgPersistence

class ArgPersistenceImplementation<T>(
    private var savedStateHandle: SavedStateHandle
) : ArgPersistence<T> {

    override fun get(argName: String): T? {
        return savedStateHandle[argName]
    }
}
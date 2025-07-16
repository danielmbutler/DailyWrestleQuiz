package com.dbtechprojects.dailywrestlequiz.data.viewmodels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

open class BaseViewModel {
    // CoroutineScope for launching async work
     protected val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
}
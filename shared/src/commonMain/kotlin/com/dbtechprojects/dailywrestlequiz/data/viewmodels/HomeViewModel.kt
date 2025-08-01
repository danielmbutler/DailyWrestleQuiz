package com.dbtechprojects.dailywrestlequiz.data.viewmodels

import com.dbtechprojects.dailywrestlequiz.data.usecase.SyncManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlin.time.measureTime


interface HomeViewModel {}

class HomeViewModelImpl(syncManager: SyncManager): BaseViewModel(), HomeViewModel {

    init {
        viewModelScope.launch(Dispatchers.IO) {
           measureTime {
               syncManager.runSync()
           }.also {
               println("sync time $it")
           }
        }
    }
}

class HomeViewModelStub: HomeViewModel {}
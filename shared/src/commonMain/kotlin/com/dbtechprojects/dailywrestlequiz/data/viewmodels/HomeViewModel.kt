package com.dbtechprojects.dailywrestlequiz.data.viewmodels

import com.dbtechprojects.dailywrestlequiz.data.usecase.SettingsUseCase
import com.dbtechprojects.dailywrestlequiz.data.usecase.SyncManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.measureTime


interface HomeViewModel {
    val streak: StateFlow<Int>
}

class HomeViewModelImpl(syncManager: SyncManager, settingsUseCase: SettingsUseCase) :
    BaseViewModel(), HomeViewModel {
    private val _streak = MutableStateFlow(0)
    override val streak get() = _streak

    init {
        viewModelScope.launch(Dispatchers.IO) {
            settingsUseCase.getStreak().collect {
                _streak.value = it
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            measureTime {
                syncManager.runSync()
            }.also {
                println("sync time $it")
            }
        }
    }
}

class HomeViewModelStub() : HomeViewModel {
    override val streak: StateFlow<Int>
        get() {
            return MutableStateFlow(1)
        }
}
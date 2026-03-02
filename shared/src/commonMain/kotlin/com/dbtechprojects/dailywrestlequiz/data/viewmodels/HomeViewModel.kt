package com.dbtechprojects.dailywrestlequiz.data.viewmodels

import com.dbtechprojects.dailywrestlequiz.data.usecase.SettingsUseCase
import com.dbtechprojects.dailywrestlequiz.data.usecase.SyncManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.measureTime


interface HomeViewModel {
    val streak: StateFlow<Int>
    val canAccessStreakMode: StateFlow<Boolean>

    fun onCreate()

    fun clearData()
}

class HomeViewModelImpl(
    private val syncManager: SyncManager,
    private val settingsUseCase: SettingsUseCase
) :
    BaseViewModel(), HomeViewModel {
    private val _streak = MutableStateFlow(0)
    override val streak get() = _streak

    private val _canAccessStreakMode = MutableStateFlow(true)
    override val canAccessStreakMode get() = _canAccessStreakMode

    private suspend fun setupSettings() {
        settingsUseCase.getSettings().collect {
            if (it != null) {
                _streak.value = it.streak
                _canAccessStreakMode.value =
                    settingsUseCase.canAccessStreakMode(it.currentStreakLastAnsweredDate)
            }
        }
    }

    override fun onCreate() {
        viewModelScope.launch(Dispatchers.IO) {
            setupSettings()
        }
        viewModelScope.launch(Dispatchers.IO) {
            measureTime {
                syncManager.runSync()
            }.also {
                println("sync time $it")
            }
        }
    }

    override fun clearData() {
        viewModelScope.launch(Dispatchers.IO) {
            settingsUseCase.clearData()
            _canAccessStreakMode.value = true
            _streak.value = 0
        }
    }
}

class HomeViewModelStub() : HomeViewModel {
    override val streak: StateFlow<Int>
        get() {
            return MutableStateFlow(1)
        }
    override val canAccessStreakMode: StateFlow<Boolean>
        get() = MutableStateFlow(false)

    override fun onCreate() {}
    override fun clearData() {}


}
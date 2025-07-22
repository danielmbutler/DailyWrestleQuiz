package com.dbtechprojects.dailywrestlequiz.data.viewmodels

import com.dbtechprojects.dailywrestlequiz.data.di.ArgPersistence
import com.dbtechprojects.dailywrestlequiz.data.model.TimeTrial
import com.dbtechprojects.dailywrestlequiz.data.usecase.TimeTrialUseCase
import com.dbtechprojects.dailywrestlequiz.data.usecase.TimerUtils
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.TimeTrialGameViewModel.Companion.ARG_TIME_TRIAL_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

interface TimeTrialGameViewModel {
    val foundItems: StateFlow<List<String>>
    val timeRemainingText: StateFlow<String>
    val events: Flow<TimeTrialGameViewModelImpl.UiEvent>
    val gameOver: StateFlow<Boolean>
    val scoreText: StateFlow<String>
    fun sendAttempt(attempt: String)

    companion object {
        const val ARG_TIME_TRIAL_ID = "timeTrialId"
    }

}

class TimeTrialGameViewModelImpl(
    private val timeTrialUseCase: TimeTrialUseCase,
    private val timerUtils: TimerUtils,
    timeTrialId: ArgPersistence<Int>
) : BaseViewModel(), TimeTrialGameViewModel {

    private var timeTrialItemsSize = 0
    private var score = 0

    private var elapsedTime = 0
    private val _timeRemainingText = MutableStateFlow("")
    override val timeRemainingText: StateFlow<String> = _timeRemainingText.asStateFlow()

    private val _eventChannel = Channel<UiEvent>()
    override val events = _eventChannel.receiveAsFlow()

    private val _gameOver = MutableStateFlow(false)
    override val gameOver: StateFlow<Boolean> = _gameOver.asStateFlow()

    private val _score = MutableStateFlow("")
    override val scoreText: StateFlow<String> = _score.asStateFlow()

    private var originalGameData: MutableMap<String, List<String>>? = null
    private val _foundItems = MutableStateFlow<List<String>>(emptyList())
    override val foundItems: StateFlow<List<String>>
        get() = _foundItems

    init {
        timeTrialId.get(ARG_TIME_TRIAL_ID)?.let { id ->
            timeTrialUseCase.getTimeTrial(id = id)?.let { timeTrial ->
                timeTrialUseCase.getDetails(id).let { namesAndAliases ->
                    _foundItems.value = namesAndAliases.map { "******" }
                    originalGameData = namesAndAliases.toMutableMap()
                    timeTrialItemsSize = namesAndAliases.size
                    _score.value = "0/$timeTrialItemsSize"
                }
                viewModelScope.launch {
                    startTimer(timeTrial.timeLimit)
                }
            }
        }
        // OH DEAR
    }


    override fun sendAttempt(attempt: String) {
        if (gameOver.value) return
        if (attempt.isBlank()) return
        if (originalGameData == null) return

        // will be filled with correct key if match found
        var answerKey: String = ""
        var index = 0

        originalGameData!!.forEach { (key, value) ->
            // check values for direct match (wrestler names)
            index++
            if (attempt.equals(key, true) ||
                // check alias
                value.find { it.equals(attempt, true) } != null
            ) {
                answerKey = key
                return@forEach
            }
        }

        if (answerKey.isNotBlank()) {
            updateForCorrectAnswer(answerKey, index)
        }
    }

    private fun updateForCorrectAnswer(answerKey: String, index: Int) {
        score++
        // solved, remove data so we dont have to loop over it again
        originalGameData?.remove(answerKey)
        // unstar key on ui side
        val current = _foundItems.value.toMutableList()
        current[index] = answerKey
        _foundItems.value = current
        // update ui
        viewModelScope.launch {
            _score.value = "$score/${timeTrialItemsSize}"
            _eventChannel.send(UiEvent.CorrectAttempt)

            if (score == timeTrialItemsSize) {
                endgame()
            }
        }
    }

    private fun endgame() {
        _gameOver.value = true
        viewModelScope.launch(Dispatchers.IO) {
            timeTrialUseCase.saveTime(elapsedTime)
        }
    }

    private suspend fun startTimer(
        totalTime: Int,
    ) {
        for (elapsed in 0..totalTime) {
            elapsedTime = elapsed
            _timeRemainingText.value = timerUtils.getTimeRemainingText(elapsed, totalTime)
            delay(1000)
        }
        // time is over
        if (!gameOver.value) {
            _gameOver.value = true
        }
    }

    sealed class UiEvent {
        object CorrectAttempt : UiEvent()
        object InCorrectAttempt : UiEvent()
    }
}
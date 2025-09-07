package com.dbtechprojects.dailywrestlequiz.data.viewmodels

import com.dbtechprojects.dailywrestlequiz.data.model.TimeTrial
import com.dbtechprojects.dailywrestlequiz.data.usecase.TimeTrialUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


interface TimeTrialListViewModel{
    val timeTrials : StateFlow<List<TimeTrial>>
}


class TimeTrialListViewModelImpl (
    timeTrialUseCase: TimeTrialUseCase,
): BaseViewModel(), TimeTrialListViewModel {


    private val _timeTrials = MutableStateFlow<List<TimeTrial>>(emptyList())

    override val timeTrials: StateFlow<List<TimeTrial>>
        get() = _timeTrials


    init {
        viewModelScope.launch(Dispatchers.IO) {
            _timeTrials.value = timeTrialUseCase.getTimeTrials()
        }
    }

}
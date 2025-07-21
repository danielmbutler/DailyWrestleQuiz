package com.dbtechprojects.dailywrestlequiz.data.viewmodels

import com.dbtechprojects.dailywrestlequiz.data.model.TimeTrial
import com.dbtechprojects.dailywrestlequiz.data.usecase.TimeTrialUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


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
        _timeTrials.value = timeTrialUseCase.getTimeTrials()
    }

}
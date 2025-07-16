package com.dbtechprojects.dailywrestlequiz.data.viewmodels

import com.dbtechprojects.dailywrestlequiz.data.model.Question
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuestionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val questionsUseCase: QuestionsUseCase
): BaseViewModel() {


    private val _state = MutableStateFlow<Question?>(null)
    val state: StateFlow<Question?> = _state.asStateFlow()

    fun requestQuestion(){
        viewModelScope.launch {
            _state.value = questionsUseCase.getRandomQuestion()
        }
    }
}
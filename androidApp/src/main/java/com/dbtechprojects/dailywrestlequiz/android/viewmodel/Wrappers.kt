package com.dbtechprojects.dailywrestlequiz.android.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dbtechprojects.dailywrestlequiz.data.model.Quiz
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuestionsUseCase
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuizUseCase
import com.dbtechprojects.dailywrestlequiz.data.usecase.TimeTrialUseCase
import com.dbtechprojects.dailywrestlequiz.data.usecase.TimerUtils
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuestionViewModel
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuestionViewModelImpl
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuizViewModelImpl
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuizViewModel
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.TimeTrialListViewModel
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.TimeTrialListViewModelImpl
import org.koin.mp.KoinPlatform.getKoin


class QuestionViewModelWrapper(
    quiz: Quiz,
    questionsUseCase: QuestionsUseCase,
    timerUtils: TimerUtils
) : ViewModel(), QuestionViewModel by QuestionViewModelImpl(questionsUseCase, quiz, timerUtils)

class QuestionViewModelFactory(private val quiz: Quiz) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return QuestionViewModelWrapper(
            quiz,
            getKoin().get(),
            getKoin().get()
        ) as T
    }
}

@Composable
fun getQuestionViewModel(quiz: Quiz): QuestionViewModel {
    return viewModel(factory =
        QuestionViewModelFactory(quiz)
    )
}

class QuizViewModelWrapper(
    quizUseCase: QuizUseCase
) : ViewModel(), QuizViewModel by QuizViewModelImpl(quizUseCase)

class QuizViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return QuizViewModelWrapper(
            getKoin().get(),
        ) as T
    }
}

@Composable
fun getQuizViewModel(): QuizViewModel {
    return viewModel(factory =
        QuizViewModelFactory()
    )
}

class TimeTrialViewModelWrapper(
    timeTrialUseCase: TimeTrialUseCase
) : ViewModel(), TimeTrialListViewModel by TimeTrialListViewModelImpl(timeTrialUseCase)

class TimeTrialListViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TimeTrialViewModelWrapper(
            getKoin().get(),
        ) as T
    }
}

@Composable
fun getTimeTrialListViewModel(): TimeTrialListViewModel {
    return viewModel(factory =
        TimeTrialListViewModelFactory()
    )
}


@file:Suppress("TYPE_INTERSECTION_AS_REIFIED_WARNING", "UNCHECKED_CAST")

package com.dbtechprojects.dailywrestlequiz.android.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dbtechprojects.dailywrestlequiz.data.di.ArgPersistence
import com.dbtechprojects.dailywrestlequiz.data.model.Quiz
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuestionsUseCase
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuizUseCase
import com.dbtechprojects.dailywrestlequiz.data.usecase.TimeTrialUseCase
import com.dbtechprojects.dailywrestlequiz.data.usecase.TimerUtils
import com.dbtechprojects.dailywrestlequiz.data.usecase.VersusModeUseCase
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.HomeViewModel
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.HomeViewModelImpl
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuestionViewModel
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuestionViewModelImpl
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuizViewModelImpl
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuizViewModel
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.TimeTrialGameViewModel
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.TimeTrialGameViewModelImpl
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.TimeTrialListViewModel
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.TimeTrialListViewModelImpl
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.VersusViewModel
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.VersusViewModelImpl
import org.koin.mp.KoinPlatform.getKoin


class QuestionViewModelWrapper(
    quizUseCase: QuizUseCase,
    questionsUseCase: QuestionsUseCase,
    args: ArgPersistence<Int?>,
    timerUtils: TimerUtils
) : ViewModel(), QuestionViewModel by QuestionViewModelImpl(
    questionsUseCase = questionsUseCase,
    quizUseCase = quizUseCase,
    args = args,
    timerUtils = timerUtils,
)

class QuestionViewModelFactory(
    private val args: ArgPersistence<Int?>,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return QuestionViewModelWrapper(
            quizUseCase = getKoin().get(),
            questionsUseCase = getKoin().get(),
            timerUtils = getKoin().get(),
            args = args,
        ) as T
    }
}

@Composable
fun getQuestionViewModel(
    args: ArgPersistence<Int?>
): QuestionViewModel {
    return viewModel(factory =
        QuestionViewModelFactory(args)
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

class TimeTrialGameViewModelWrapper(
    timeTrialUseCase: TimeTrialUseCase,
    timerUtils: TimerUtils,
    timeTrialId: ArgPersistence<Int>
) : ViewModel(), TimeTrialGameViewModel by
TimeTrialGameViewModelImpl(
    timeTrialUseCase,
    timerUtils = timerUtils,
    timeTrialId = timeTrialId
)

class TimeTrialGameViewModelFactory(private val timeTrialId: ArgPersistence<Int>) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TimeTrialGameViewModelWrapper(
            getKoin().get(),
            getKoin().get(),
            timeTrialId = timeTrialId
        ) as T
    }
}

@Composable
fun getTimeTrialGameViewModel(id: ArgPersistence<Int>): TimeTrialGameViewModel {
    return viewModel(factory =
        TimeTrialGameViewModelFactory(id)
    )
}

class HomeViewModelWrapper(
) : ViewModel(), HomeViewModel by
HomeViewModelImpl(getKoin().get(), getKoin().get())

class HomeViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModelWrapper() as T
    }
}

@Composable
fun getHomeViewModel(): HomeViewModel {
    return viewModel(factory =
        HomeViewModelFactory()
    )
}


class VersusViewModelWrapper(
    versusUseCase: VersusModeUseCase
) : ViewModel(), VersusViewModel by VersusViewModelImpl(versusUseCase)

class VersusViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VersusViewModelWrapper(
            getKoin().get(),
        ) as T
    }
}

@Composable
fun getVersusViewModel(): VersusViewModel {
    return viewModel(factory =
        VersusViewModelFactory()
    )
}


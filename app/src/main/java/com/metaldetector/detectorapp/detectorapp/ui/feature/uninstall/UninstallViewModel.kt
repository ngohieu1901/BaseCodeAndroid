package com.metaldetector.detectorapp.detectorapp.ui.feature.uninstall

import androidx.lifecycle.viewModelScope
import com.metaldetector.detectorapp.detectorapp.R
import com.metaldetector.detectorapp.detectorapp.base.BaseViewModel
import com.metaldetector.detectorapp.detectorapp.di.IoDispatcher
import com.metaldetector.detectorapp.detectorapp.model.AnswerModel
import com.metaldetector.detectorapp.detectorapp.ui_intent.UninstallUiIntent
import com.metaldetector.detectorapp.detectorapp.ui_state.UninstallUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UninstallViewModel @Inject constructor(@IoDispatcher private val ioDispatcher: CoroutineDispatcher): BaseViewModel<UninstallUiState, UninstallUiIntent>() {
    override fun initState(): UninstallUiState = UninstallUiState.Idle

    override fun processIntent(intent: UninstallUiIntent) {
        when (intent) {
            is UninstallUiIntent.Insert -> insertListAnswer()
            is UninstallUiIntent.Update -> updateListAnswer(intent.data)
        }
    }

    private fun insertListAnswer() {
        viewModelScope.launch (ioDispatcher + exceptionHandler) {
            dispatchStateUi(UninstallUiState.Loading)
            try {
                val listData = mutableListOf<AnswerModel>().apply {
                    add(AnswerModel(name = R.string.difficult_to_use, isSelected = true))
                    add(AnswerModel(name = R.string.too_many_ads))
                    add(AnswerModel(name = R.string.error_not_working))
                    add(AnswerModel(name = R.string.fast_battery_drain))
                    add(AnswerModel(name = R.string.others))
                }
                dispatchStateUi(UninstallUiState.Success(listAnswer = listData))
            } catch (exception: Exception) {
                dispatchError(exception)
            }
        }
    }

    private fun updateListAnswer(data: AnswerModel) {
        viewModelScope.launch(ioDispatcher + exceptionHandler) {
            try {
                val listUpdated = currentState.let { currentState ->
                    if (currentState is UninstallUiState.Success) {
                        currentState.listAnswer.map {
                            if (data.name == it.name) {
                                it.copy(isSelected = true)
                            } else {
                                it.copy(isSelected = false)
                            }
                        }
                    } else {
                        emptyList()
                    }
                }
                dispatchStateUi(UninstallUiState.Success(listAnswer = listUpdated))
            } catch (e: Exception) {
                dispatchError(e)
            }
        }
    }
}
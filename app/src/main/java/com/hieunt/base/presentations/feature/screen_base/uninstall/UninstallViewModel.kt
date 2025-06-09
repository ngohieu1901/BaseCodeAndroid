package com.hieunt.base.presentations.feature.screen_base.uninstall

import androidx.lifecycle.viewModelScope
import com.hieunt.base.R
import com.hieunt.base.base.BaseMvvmViewModel
import com.hieunt.base.di.IoDispatcher
import com.hieunt.base.domain.model.AnswerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UninstallViewModel @Inject constructor(@IoDispatcher private val ioDispatcher: CoroutineDispatcher): BaseMvvmViewModel<UninstallUiState>() {
    override fun initState(): UninstallUiState = UninstallUiState()

    init {
        viewModelScope.launch (ioDispatcher + exceptionHandler) {
            val listData = mutableListOf<AnswerModel>().apply {
                add(AnswerModel(name = R.string.difficult_to_use, isSelected = true))
                add(AnswerModel(name = R.string.too_many_ads))
                add(AnswerModel(name = R.string.error_not_working))
                add(AnswerModel(name = R.string.fast_battery_drain))
                add(AnswerModel(name = R.string.others))
            }
            dispatchStateUi(currentState.copy(listAnswer = listData))
        }
    }

    fun updateListAnswer(data: AnswerModel) {
        viewModelScope.launch (ioDispatcher + exceptionHandler) {
            val listUpdated = currentState.listAnswer.map {
                if (data.name == it.name) {
                    it.copy(isSelected = true)
                } else {
                    it.copy(isSelected = false)
                }
            }
            dispatchStateUi(currentState.copy(listAnswer = listUpdated))
        }
    }
}
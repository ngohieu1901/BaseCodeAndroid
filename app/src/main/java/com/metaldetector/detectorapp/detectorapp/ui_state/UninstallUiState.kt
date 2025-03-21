package com.metaldetector.detectorapp.detectorapp.ui_state

import com.metaldetector.detectorapp.detectorapp.model.AnswerModel

sealed class UninstallUiState {
    object Idle : UninstallUiState()
    object Loading : UninstallUiState()
    data class Success(val listAnswer: List<AnswerModel>) : UninstallUiState()
    data class Error(val exception: Throwable) : UninstallUiState()
}
package com.metaldetector.detectorapp.detectorapp.ui_intent

import com.metaldetector.detectorapp.detectorapp.model.AnswerModel

sealed class UninstallUiIntent {
    object Insert : UninstallUiIntent()
    data class Update(val data: AnswerModel) : UninstallUiIntent()
}
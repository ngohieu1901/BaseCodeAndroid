package com.hieunt.base.presentations.feature.screen_base.language_start

import com.hieunt.base.domain.model.LanguageModelNew

data class LanguageStartUiState(
    var listLanguage: List<LanguageModelNew> = emptyList(),
)
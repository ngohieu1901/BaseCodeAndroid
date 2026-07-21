package com.hieunt.base.presentations.model

data class LanguageParentModel(
    val languageName: String,
    val isoLanguage: String,
    val isCheck: Boolean,
    val image: Int,
    val listLanguageSubModel: List<LanguageSubModel>,
    val isExpand: Boolean = false
)
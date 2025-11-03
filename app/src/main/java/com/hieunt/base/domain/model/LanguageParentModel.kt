package com.hieunt.base.domain.model

data class LanguageParentModel(
    var languageName: String,
    var isoLanguage: String,
    var isCheck: Boolean,
    var image: Int? = null,
    var listLanguageSubModel: MutableList<LanguageSubModel>,
    var isExpand: Boolean = false
)
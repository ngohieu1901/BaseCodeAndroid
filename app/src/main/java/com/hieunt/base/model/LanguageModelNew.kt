package com.hieunt.base.model

data class LanguageModelNew(
    var languageName: String,
    var isoLanguage: String,
    var isCheck: Boolean,
    var image: Int? = null,
    var isShowAnim: Boolean = false,
){
    constructor() : this("", "", false, 0, isShowAnim = false) {}
}
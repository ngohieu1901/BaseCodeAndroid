package com.metaldetector.detectorapp.detectorapp.model

data class LanguageModelNew(
    var languageName: String,
    var isoLanguage: String,
    var isCheck: Boolean,
    var image: Int? = null
){
    constructor() : this("", "", false, 0) {}
}
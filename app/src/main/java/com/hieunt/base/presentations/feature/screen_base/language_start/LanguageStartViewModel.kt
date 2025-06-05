package com.hieunt.base.presentations.feature.screen_base.language_start

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.hieunt.base.R
import com.hieunt.base.base.BaseViewModel
import com.hieunt.base.di.IoDispatcher
import com.hieunt.base.domain.model.LanguageModelNew
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageStartViewModel @Inject constructor(@IoDispatcher private val ioDispatcher: CoroutineDispatcher): BaseViewModel<LanguageStartUiState>() {
    override fun initState(): LanguageStartUiState = LanguageStartUiState()

    init {
        initListLanguage()
    }

    private fun initListLanguage() {
        viewModelScope.launch(exceptionHandler + ioDispatcher) {
            val lists: MutableList<LanguageModelNew> = ArrayList()
            lists.add(LanguageModelNew("Español", "es", false, R.drawable.ic_span_flag))
            lists.add(LanguageModelNew("Français", "fr", false, R.drawable.ic_french_flag))
            lists.add(LanguageModelNew("हिन्दी", "hi", false, R.drawable.ic_hindi_flag))
            lists.add(LanguageModelNew("English", "en", false, R.drawable.ic_english_flag))
            lists.add(LanguageModelNew("Português (Brazil)", "pt-rBR", false, R.drawable.ic_brazil_flag))
            lists.add(LanguageModelNew("Português (Portu)", "pt-rPT", false, R.drawable.ic_portuguese_flag))
            lists.add(LanguageModelNew("日本語", "ja", false, R.drawable.ic_japan_flag))
            lists.add(LanguageModelNew("Deutsch", "de", false, R.drawable.ic_german_flag))
            lists.add(LanguageModelNew("中文 (简体)", "zh-rCN", false, R.drawable.ic_china_flag))
            lists.add(LanguageModelNew("中文 (繁體) ", "zh-rTW", false, R.drawable.ic_china_flag))
            lists.add(LanguageModelNew("عربي ", "ar", false, R.drawable.ic_a_rap_flag))
            lists.add(LanguageModelNew("বাংলা ", "bn", false, R.drawable.ic_bengali_flag))
            lists.add(LanguageModelNew("Русский ", "ru", false, R.drawable.ic_russia_flag))
            lists.add(LanguageModelNew("Türkçe ", "tr", false, R.drawable.ic_turkey_flag))
            lists.add(LanguageModelNew("한국인 ", "ko", false, R.drawable.ic_korean_flag))
            lists.add(LanguageModelNew("Indonesia", "in", false, R.drawable.flag_indonesia))

            val systemLang: String = Resources.getSystem().configuration.locales[0].language
            Log.e("lllllllllllllllll", "systemLang: $systemLang")
            val systemLangModel = lists.find { it.isoLanguage.contains(systemLang) }
            systemLangModel?.let {
                Log.e("lllllllllllllllll", "systemLangModel: $it")
                lists.remove(it)
                lists.add(3, it.copy(isShowAnim = true))
            } ?: lists.set(3, lists[3].copy(isShowAnim = true))

            dispatchStateUi(currentState.copy(listLanguage = lists))
        }
    }

    fun setSelectLanguage(isoLanguage: String) {
        viewModelScope.launch(exceptionHandler + ioDispatcher) {
            val listUpdate = currentState.listLanguage.map {
                it.copy(isShowAnim = false, isCheck = it.isoLanguage == isoLanguage)
            }
            dispatchStateUi(currentState.copy(listLanguage = listUpdate))
        }
    }
}
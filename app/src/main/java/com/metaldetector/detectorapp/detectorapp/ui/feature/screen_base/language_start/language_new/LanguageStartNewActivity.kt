package com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.language_start.language_new
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.Log
import com.metaldetector.detectorapp.detectorapp.R
import com.metaldetector.detectorapp.detectorapp.base.BaseActivity
import com.metaldetector.detectorapp.detectorapp.databinding.ActivityLanguageStartBinding
import com.metaldetector.detectorapp.detectorapp.model.LanguageModelNew
import com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.intro.IntroActivity
import com.metaldetector.detectorapp.detectorapp.utils.SystemUtils
import com.metaldetector.detectorapp.detectorapp.view_model.CommonVM
import com.metaldetector.detectorapp.detectorapp.widget.launchActivity
import com.metaldetector.detectorapp.detectorapp.widget.tap
import com.metaldetector.detectorapp.detectorapp.widget.visible
import java.util.Locale

class LanguageStartNewActivity : BaseActivity<ActivityLanguageStartBinding, CommonVM>() , IClickLanguage {

    private var adapter: LanguageStartNewAdapter ?= null
    private var model: LanguageModelNew = LanguageModelNew()


    override fun setViewBinding():ActivityLanguageStartBinding  {
        return ActivityLanguageStartBinding.inflate(layoutInflater)
    }

    override fun initViewModel(): Class<CommonVM> = CommonVM::class.java

    override fun onBackPressedSystem() {
        finishAffinity()
    }

    override fun initView() {
        adapter = LanguageStartNewAdapter(
            setLanguageDefault(),
            this,
        )
        binding.recyclerView.adapter = adapter
        binding.ivDone.tap {
            sharePref.isFirstSelectLanguage = false
            SystemUtils.setPreLanguage(this@LanguageStartNewActivity, model.isoLanguage)
            SystemUtils.setLocale(this)
            launchActivity(IntroActivity::class.java)
            finish()
        }
    }

    override fun dataCollect() {

    }

    override fun onClick(data: LanguageModelNew) {
        model = data
        SystemUtils.setLocale(this)
        binding.tvSelectLanguage.text = getLocalizedString(this, model.isoLanguage, R.string.please_select_language_to_continue)
        binding.tvLanguage.text = getLocalizedString(this, model.isoLanguage, R.string.Language)
        binding.ivDone.visible()
    }

    private fun setLanguageDefault(): List<LanguageModelNew> {
        val lists: MutableList<LanguageModelNew> = ArrayList()
        val systemLang: String = Resources.getSystem().configuration.locales[0].language
        Log.d("CHECK_systemLang", "systemLang: $systemLang ")

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
        lists.add(LanguageModelNew("Indonesia", "in", false, R.drawable.ic_indo_flag))

        val systemLangModel = lists.find { it.isoLanguage.contains(systemLang) }
        systemLangModel?.let {
            lists.remove(it)
            lists.add(3, it)
        }

        return lists
    }

    private fun getLocalizedString(context: Context, languageCode: String, resId: Int): String {

        val localeParts = languageCode.split("-")
        val myLocale = if (localeParts.size > 1) {
            Locale(localeParts[0], localeParts[1])
        } else {
            Locale(languageCode)
        }
        val config = Configuration(context.resources.configuration)
        config.setLocale(myLocale)
        val localizedContext = context.createConfigurationContext(config)
        return localizedContext.resources.getString(resId)
    }
}
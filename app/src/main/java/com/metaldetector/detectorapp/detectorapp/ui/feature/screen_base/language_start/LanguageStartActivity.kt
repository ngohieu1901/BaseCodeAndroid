package com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.language_start

import com.metaldetector.detectorapp.detectorapp.R
import com.metaldetector.detectorapp.detectorapp.base.BaseActivity
import com.metaldetector.detectorapp.detectorapp.databinding.ActivityLanguageStartBinding
import com.metaldetector.detectorapp.detectorapp.firebase.ads.RemoteName.NATIVE_LANG
import com.metaldetector.detectorapp.detectorapp.firebase.event.EventName
import com.metaldetector.detectorapp.detectorapp.model.LanguageModel
import com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.intro.IntroActivity
import com.metaldetector.detectorapp.detectorapp.utils.SharePrefUtils
import com.metaldetector.detectorapp.detectorapp.utils.SystemUtils
import com.metaldetector.detectorapp.detectorapp.view_model.CommonVM
import com.metaldetector.detectorapp.detectorapp.widget.invisible
import com.metaldetector.detectorapp.detectorapp.widget.launchActivity
import com.metaldetector.detectorapp.detectorapp.widget.logEvent
import com.metaldetector.detectorapp.detectorapp.widget.tap
import com.metaldetector.detectorapp.detectorapp.widget.visible
import java.util.Locale

class LanguageStartActivity : BaseActivity<ActivityLanguageStartBinding, CommonVM>() {

    private lateinit var adapter: LanguageStartAdapter
    private var listLanguage: ArrayList<LanguageModel> = ArrayList()
    private val sharePrefUtils by lazy { SharePrefUtils(this) }
    private var codeLang = ""


    override fun setViewBinding():ActivityLanguageStartBinding  {
        return ActivityLanguageStartBinding.inflate(layoutInflater)
    }

    override fun initViewModel(): Class<CommonVM> = CommonVM::class.java

    override fun onBackPressedSystem() {
        finishAffinity()
    }

    override fun initView() {
        logEvent(EventName.language_fo_open)

        loadNative(
            NATIVE_LANG,
            R.layout.ads_shimmer_language,
            R.layout.ads_native_language,
        )

        setCodeLanguage()

        if (sharePrefUtils.isFirstSelectLanguage){
            binding.ivDone.invisible()
        }

        adapter = LanguageStartAdapter(onClick = {
            if (sharePrefUtils.isFirstSelectLanguage){
                binding.ivDone.visible()
            }
            codeLang = it.code
            adapter.setCheck(it.code)
        })

        binding.recyclerView.adapter = adapter
        binding.ivDone.tap {
            sharePrefUtils.isFirstSelectLanguage = false
            logEvent(EventName.language_fo_save_click)
            SystemUtils.saveLocale(baseContext, codeLang)
            launchActivity(IntroActivity::class.java)
            finish()
        }
        initData()
    }

    override fun dataCollect() {

    }

    private fun setCodeLanguage() {
        //language
        val codeLangDefault = Locale.getDefault().language
        val langDefault = arrayOf("fr", "pt", "es", "de", "hi", "in", "en")
        codeLang =
            if (SystemUtils.getPreLanguage(this) == "")
                if (!mutableListOf(*langDefault).contains(codeLangDefault)) "en"
                else codeLangDefault
            else {
                SystemUtils.getPreLanguage(this)
            }
    }

    private fun initData() {
        listLanguage.clear()
        listLanguage.addAll(SystemUtils.listLanguage())

        if (!sharePrefUtils.isFirstSelectLanguage){
            var pos = 0
            listLanguage.forEachIndexed { index, languageModel ->
                if (languageModel.code == codeLang) {
                    pos = index
                    return@forEachIndexed
                }
            }
            val temp = listLanguage[pos]
            temp.active = true
            listLanguage.removeAt(pos)
            listLanguage.add(0, temp)
        }

        adapter.addList(listLanguage)
    }
}
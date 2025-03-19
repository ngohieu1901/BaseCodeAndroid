package com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.language

import com.metaldetector.detectorapp.detectorapp.base.BaseActivity
import com.metaldetector.detectorapp.detectorapp.databinding.ActivityLanguageBinding
import com.metaldetector.detectorapp.detectorapp.model.LanguageModel
import com.metaldetector.detectorapp.detectorapp.ui.feature.main.MainActivity
import com.metaldetector.detectorapp.detectorapp.utils.SystemUtils
import com.metaldetector.detectorapp.detectorapp.view_model.CommonVM
import com.metaldetector.detectorapp.detectorapp.widget.invisible
import com.metaldetector.detectorapp.detectorapp.widget.launchActivity
import com.metaldetector.detectorapp.detectorapp.widget.tap
import java.util.Locale

class LanguageActivity : BaseActivity<ActivityLanguageBinding, CommonVM>() {
    private var listLanguage: ArrayList<LanguageModel> = ArrayList()
    private lateinit var adapter: LanguageAdapter
    private var lang = "en"
    override fun setViewBinding(): ActivityLanguageBinding {
        return ActivityLanguageBinding.inflate(layoutInflater)
    }

    override fun initViewModel(): Class<CommonVM> = CommonVM::class.java

    override fun initView() {
        loadBanner()
        setCodeLanguage()

        adapter = LanguageAdapter(onClick = {
            lang = it.code
            adapter.setCheck(it.code)
            selectLanguage()
        })
        binding.recyclerView.adapter = adapter
        binding.ivBack.tap {
            finish()
        }
        binding.ivCheck.tap { selectLanguage() }
        binding.ivCheck.invisible()

        initData()
    }

    override fun dataCollect() {

    }

    private fun selectLanguage() {
        SystemUtils.saveLocale(this, lang)
        launchActivity(MainActivity::class.java)
        finishAffinity()
    }

    private fun setCodeLanguage() {
        //language
        val codeLangDefault = Locale.getDefault().language
        val langDefault = arrayOf("fr", "pt", "es", "de", "hi", "in", "en")
        lang =
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

        var pos = 0
        listLanguage.forEachIndexed { index, languageModel ->
            if (languageModel.code == lang) {
                pos = index
                return@forEachIndexed
            }
        }
        val temp = listLanguage[pos]
        temp.active = true
        listLanguage.removeAt(pos)
        listLanguage.add(0, temp)

        adapter.addList(listLanguage)
    }

}
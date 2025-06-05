package com.hieunt.base.presentations.feature.screen_base.language_start

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.amazic.library.ads.admob.Admob
import com.amazic.library.ads.admob.AdmobApi
import com.amazic.library.ads.callback.BannerCallback
import com.hieunt.base.R
import com.hieunt.base.base.BaseActivity
import com.hieunt.base.databinding.ActivityLanguageStartBinding
import com.hieunt.base.firebase.ads.RemoteName
import com.hieunt.base.firebase.ads.RemoteName.NATIVE_LANG
import com.hieunt.base.firebase.ads.RemoteName.NATIVE_LANG_2
import com.hieunt.base.firebase.event.EventName
import com.hieunt.base.firebase.event.ParamName
import com.hieunt.base.presentations.feature.screen_base.intro.IntroActivity
import com.hieunt.base.utils.SharePrefUtils
import com.hieunt.base.utils.SystemUtils
import com.hieunt.base.widget.launchActivity
import com.hieunt.base.widget.logEvent
import com.hieunt.base.widget.tap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class LanguageStartActivity : BaseActivity<ActivityLanguageStartBinding>() {
    private val viewModel : LanguageStartViewModel by viewModels()
    private lateinit var adapter: LanguageStartAdapter
    private var isoLanguage : String = ""
    private var nameLanguage : String = ""
    private var isSelectedLanguage = false

    @Inject
    lateinit var sharePref: SharePrefUtils

    override fun setViewBinding():ActivityLanguageStartBinding  {
        return ActivityLanguageStartBinding.inflate(layoutInflater)
    }

    override fun onBackPressedSystem() {
        finishAffinity()
    }

    override fun initView() {
        logEvent(EventName.language_fo_open)
        if (sharePref.countOpenApp <= 10) {
            logEvent(EventName.language_fo_open + "_" + sharePref.countOpenApp)
        }

        Admob.getInstance().loadBannerAds(
            this,
            AdmobApi.getInstance().getListIDByName(RemoteName.BANNER_SETTING),
            binding.bannerSetting,
            object : BannerCallback() {},
            {},
            RemoteName.BANNER_SETTING
        )

        loadDoubleNative(
            NATIVE_LANG,
            NATIVE_LANG,
            NATIVE_LANG_2,
            R.layout.native_large_language_custom,
            R.layout.shimmer_native_large_language_custom,
        )

        binding.cvSave.apply {
            setCardBackgroundColor(Color.parseColor("#D1D5DB"))
            isEnabled = false
        }

        adapter = LanguageStartAdapter(onClick = {
            logEvent(EventName.language_fo_choose)
            if (sharePref.countOpenApp <= 10 && !isSelectedLanguage) {
                logEvent(EventName.language_fo_choose + "_" + sharePref.countOpenApp)
            }
            if(!isSelectedLanguage){
                loadDoubleNative(
                    NATIVE_LANG,
                    NATIVE_LANG,
                    NATIVE_LANG_2,
                    R.layout.native_large_language_custom,
                    R.layout.shimmer_native_large_language_custom,
                )
            }
            isSelectedLanguage = true
            SystemUtils.setLocale(this)
            isoLanguage = it.isoLanguage
            nameLanguage = it.languageName
            viewModel.setSelectLanguage(it.isoLanguage)
            binding.tvSelectLanguage.text = getLocalizedString(this, isoLanguage, R.string.please_select_language_to_continue)
            binding.tvLanguage.text = getLocalizedString(this, isoLanguage, R.string.Language)
            binding.tvSave.text = getLocalizedString(this, isoLanguage, R.string.save)
            binding.cvSave.apply {
                setCardBackgroundColor(Color.parseColor("#E42427"))
                isEnabled = true
            }
        })

        binding.recyclerView.adapter = adapter

        binding.cvSave.tap {
            logEvent(EventName.language_fo_save_click, bundle = Bundle().apply {putString(ParamName.language_name, nameLanguage)})
            if (sharePref.countOpenApp <= 10) {
                logEvent(EventName.language_fo_save_click + "_" + sharePref.countOpenApp)
            }
            sharePref.isFirstSelectLanguage = false
            SystemUtils.setPreLanguage(this@LanguageStartActivity, isoLanguage)
            SystemUtils.setLocale(this)
            launchActivity(IntroActivity::class.java)
            finish()
        }
    }

    override fun dataCollect() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiStore.collect {
                    launch {
                        adapter.submitList(it.listLanguage)
                    }
                }
            }
        }
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
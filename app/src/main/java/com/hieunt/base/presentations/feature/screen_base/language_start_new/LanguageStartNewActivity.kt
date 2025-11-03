package com.hieunt.base.presentations.feature.screen_base.language_start_new

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.amazic.library.Utils.EventTrackingHelper
import com.amazic.library.ads.admob.Admob
import com.amazic.library.ads.admob.AdmobApi
import com.amazic.library.ads.callback.BannerCallback
import com.amazic.library.ads.callback.InterCallback
import com.amazic.library.ads.callback.NativeCallback
import com.amazic.library.organic.TechManager
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.hieunt.base.R
import com.hieunt.base.base.BaseActivity
import com.hieunt.base.databinding.ActivityLanguageStartNewBinding
import com.hieunt.base.domain.model.LanguageParentModel
import com.hieunt.base.domain.model.LanguageSubModel
import com.hieunt.base.firebase.ads.RemoteName
import com.hieunt.base.firebase.ads.RemoteName.NATIVE_LANG
import com.hieunt.base.firebase.ads.RemoteName.NATIVE_LANG_2
import com.hieunt.base.firebase.event.EventName
import com.hieunt.base.firebase.event.ParamName
import com.hieunt.base.presentations.feature.screen_base.intro.IntroActivity
import com.hieunt.base.presentations.feature.screen_base.splash.SplashActivity
import com.hieunt.base.presentations.feature.screen_base.splash.SplashActivity.Companion.isShowNativeClickLanguagePreloadAtSplash
import com.hieunt.base.presentations.feature.screen_base.splash.SplashActivity.Companion.isShowNativeLanguagePreloadAtSplash
import com.hieunt.base.presentations.feature.screen_base.splash.SplashActivity.Companion.nativeLanguageClickPreload
import com.hieunt.base.presentations.feature.screen_base.splash.SplashActivity.Companion.nativeLanguagePreload
import com.hieunt.base.utils.SharePrefUtils
import com.hieunt.base.utils.SystemUtils
import com.hieunt.base.widget.launchActivity
import com.hieunt.base.widget.logEvent
import com.hieunt.base.widget.tap
import com.hieunt.base.widget.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LanguageStartNewActivity: BaseActivity<ActivityLanguageStartNewBinding>(
    ActivityLanguageStartNewBinding::inflate
) {
    @Inject
    lateinit var sharePref: SharePrefUtils

    private var isPause = false
    private var adapter: LanguageStartNewAdapter? = null
    private var model = LanguageSubModel()
    private var isChooseLanguage = false

    companion object {
        var isLogEventLanguageUserView = false
        var nativeIntroPreload: NativeAd? = null
        var isShowNativeIntroPreloadAtSplash = false
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

        val nativeManager = loadNative(
            remoteKey = NATIVE_LANG,
            remoteKeySecondary = NATIVE_LANG_2,
            adsKeyMain = NATIVE_LANG,
            adsKeySecondary = NATIVE_LANG_2,
            idLayoutNative = R.layout.ads_native_large_button_above,
            idLayoutShimmer = R.layout.ads_shimmer_large_button_above,
            isAlwaysReloadOnResume = false
        )

        preloadANativeMainIntro()

        adapter = LanguageStartNewAdapter(
            setLanguageDefault(),
            onClickSubLanguage = {
                logEvent(EventName.language_fo_choose)
                if (sharePref.countOpenApp <= 10 && !isChooseLanguage) {
                    logEvent(EventName.language_fo_choose + "_" + sharePref.countOpenApp)
                }
                SystemUtils.setLocale(this)
                binding.ivDone.visible()
                binding.ivDone.isEnabled = true
                model = it
                //---------
                val isArabic = model.isoLanguage == "ar"
                window.decorView.layoutDirection =
                    if (isArabic) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR
            },
            onClickDropDown = {
                nativeManager?.cancelAutoReloadNative()
                showNativeClickLanguagePreloadAtSplash()
            }
        )

        binding.recyclerView.adapter = adapter

        binding.ivDone.tap {
            binding.llApplyLanguage.visibility = View.VISIBLE
            if (Admob.getInstance().checkCondition(this, "inter_splash") &&
                !TechManager.getInstance().isTech(this) &&
                Admob.getInstance().interstitialAdSplash != null
            ) {
                Admob.getInstance().showInterAds(this, Admob.getInstance().interstitialAdSplash, object : InterCallback() {
                    override fun onNextAction() {
                        super.onNextAction()
                        startNextAct()
                    }
                }, false, "inter_splash")
            } else {
                startNextAct()
            }
        }
    }

    private fun startNextAct() {
        logEvent(EventName.language_fo_save_click, bundle = Bundle().apply {putString(ParamName.language_name, model.languageName)})
        if (sharePref.countOpenApp <= 10) {
            logEvent(EventName.language_fo_save_click + "_" + sharePref.countOpenApp)
        }
        sharePref.isFirstSelectLanguage = false
        SystemUtils.setPreLanguage(this@LanguageStartNewActivity, model.isoLanguage)
        SystemUtils.setLocale(this)
        launchActivity(IntroActivity::class.java)
        finish()
    }

    override fun dataCollect() {

    }

    private fun preloadANativeMainIntro() {
        Admob.getInstance().loadNativeAds(
            this,
            AdmobApi.getInstance().getListIDByName(RemoteName.NATIVE_INTRO),
            object : NativeCallback() {
                override fun onNativeAdLoaded(nativeAd: NativeAd?) {
                    super.onNativeAdLoaded(nativeAd)
                    nativeIntroPreload = nativeAd
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    isShowNativeIntroPreloadAtSplash = true
                }
            }, RemoteName.NATIVE_INTRO
        )
    }

    override fun onPause() {
        super.onPause()
        isPause = true
    }

    override fun onResume() {
        super.onResume()
        isPause = false
        Handler(Looper.getMainLooper()).postDelayed({
            if (!isPause) {
                showNativeLanguagePreloadAtSplash()
            }
            if (SplashActivity.isShowSplashAds) {
                if (SplashActivity.isCloseSplashAds) {
                    if (!isLogEventLanguageUserView && !isPause) {
                        EventTrackingHelper.logEvent(this, "language_user_view")
                        if (sharePref.countOpenApp <= 10) {
                            EventTrackingHelper.logEvent(this, "language_user_view" + "_${sharePref.countOpenApp}")
                            isLogEventLanguageUserView = true
                        }
                    }
                }
            } else {
                if (isLogEventLanguageUserView && !isPause) {
                    EventTrackingHelper.logEvent(this, "language_user_view")
                    if (sharePref.countOpenApp <= 10) {
                        EventTrackingHelper.logEvent(this, "language_user_view" + "_${sharePref.countOpenApp}")
                        isLogEventLanguageUserView = true
                    }
                }
            }
        }, 1000)
    }

    private fun showNativeLanguagePreloadAtSplash() {
        if (nativeLanguagePreload != null && !isShowNativeLanguagePreloadAtSplash && !TechManager.getInstance().isTech(this)) {
            val adView: NativeAdView = layoutInflater.inflate(R.layout.ads_native_large_button_above, binding.frAds, false) as NativeAdView
            binding.frAds.addView(adView)
            Admob.getInstance().populateNativeAdView(nativeLanguagePreload, adView)
        }
    }

    private fun showNativeClickLanguagePreloadAtSplash() {
        if (nativeLanguageClickPreload != null && !isShowNativeClickLanguagePreloadAtSplash) {
            val adView: NativeAdView = layoutInflater.inflate(
                R.layout.ads_native_large_button_above,
                binding.frAds,
                false
            ) as NativeAdView
            binding.frAds.addView(adView)
            Admob.getInstance().populateNativeAdView(nativeLanguageClickPreload, adView)
        }
    }

    private fun setLanguageDefault(): List<LanguageParentModel> {
        val lists: MutableList<LanguageParentModel> = ArrayList()
        lists.add(
            LanguageParentModel(
                "English", "en", false, R.drawable.ic_english_flag,
                mutableListOf(
                    LanguageSubModel(R.drawable.flag_el_uk,"English (UK)", "en", false),
                    LanguageSubModel(R.drawable.flag_el_us,"English (US)", "en", false),
                    LanguageSubModel(R.drawable.flag_el_india,"English (India)", "en", false),
                    LanguageSubModel(R.drawable.flag_el_international,"English (International)", "en", false)
                )
            )
        )
        lists.add(
            LanguageParentModel(
                "Hindi", "hi", false, R.drawable.ic_hindi_flag,
                mutableListOf(
                    LanguageSubModel(R.drawable.flag_hindi_india,"Hindi (Standard – India)", "hi", false),
                    LanguageSubModel(R.drawable.flag_hindi_el,"Hindi (Hinglish)", "hi", false)
                )
            )
        )
        lists.add(
            LanguageParentModel(
                "Spanish", "es", false, R.drawable.ic_span_flag,
                mutableListOf(
                    LanguageSubModel(R.drawable.flag_spain_spain,"Spanish (Spain)", "es", false),
                    LanguageSubModel(R.drawable.flag_spain_latin,"Spanish (Latin America)", "es", false),
                    LanguageSubModel(R.drawable.flag_spain_mexico,"Spanish (Mexico)", "es", false)
                )
            )
        )
        lists.add(
            LanguageParentModel(
                "French", "fr", false, R.drawable.ic_french_flag,
                mutableListOf(
                    LanguageSubModel(R.drawable.flag_fr_fr,"French (France)", "fr", false),
                    LanguageSubModel(R.drawable.flag_fr_canada,"French (Canada)", "fr", false),
                    LanguageSubModel(R.drawable.flag_fr_afica,"French (Africa)", "fr", false)
                )
            )
        )
        lists.add(
            LanguageParentModel(
                "German", "de", false, R.drawable.ic_german_flag,
                mutableListOf(
                    LanguageSubModel(R.drawable.flag_de_de,"German (Germany)", "de", false),
                    LanguageSubModel(R.drawable.flag_de_austria,"German (Austria)", "de", false),
                    LanguageSubModel(R.drawable.flag_de_switzer,"German (Switzerland)", "de", false)
                )
            )
        )
        lists.add(
            LanguageParentModel(
                "Indonesian", "in", false, R.drawable.ic_indo_flag,
                mutableListOf(
                    LanguageSubModel(R.drawable.flag_indo_spain,"Indonesian (Standard)", "in", false),
//                    LanguageSubModel(R.drawable.flag_indo_spain,"Indonesian (Informal, English combined)", "in", false),
                    LanguageSubModel(R.drawable.flag_indo_japan,"Indonesian (Javanese-influenced)", "in", false)
                )
            )
        )
        lists.add(
            LanguageParentModel(
                "Portuguese", "pt", false, R.drawable.ic_portuguese_flag,
                mutableListOf(
                    LanguageSubModel(R.drawable.flag_pt_pt,"Portuguese (Portugal)", "pt", false),
                    LanguageSubModel(R.drawable.flag_pt_brazil,"Portuguese (Brazil)", "pt", false),
                    LanguageSubModel(R.drawable.flag_pt_afica,"Portuguese (Africa)", "pt", false)
                )
            )
        )
        
        return lists
    }
}
package com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.amazic.library.Utils.NetworkUtil
import com.amazic.library.ads.admob.Admob
import com.amazic.library.ads.callback.AppOpenCallback
import com.amazic.library.ads.callback.InterCallback
import com.amazic.library.ads.splash_ads.AsyncSplash
import com.metaldetector.detectorapp.detectorapp.R
import com.metaldetector.detectorapp.detectorapp.base.BaseActivity
import com.metaldetector.detectorapp.detectorapp.databinding.ActivitySplashBinding
import com.metaldetector.detectorapp.detectorapp.firebase.ads.RemoteName.BANNER_SPLASH
import com.metaldetector.detectorapp.detectorapp.firebase.ads.RemoteName.TURN_OFF_CONFIGS
import com.metaldetector.detectorapp.detectorapp.firebase.event.EventName
import com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.language_start.LanguageStartActivity
import com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.no_internet.NoInternetActivity
import com.metaldetector.detectorapp.detectorapp.value.Default.IntentKeys.SCREEN
import com.metaldetector.detectorapp.detectorapp.value.Default.IntentKeys.SPLASH_ACTIVITY
import com.metaldetector.detectorapp.detectorapp.view_model.CommonVM
import com.metaldetector.detectorapp.detectorapp.widget.launchActivity
import com.metaldetector.detectorapp.detectorapp.widget.logEvent

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding, CommonVM>() {

    private val interCallback = object : InterCallback() {
        override fun onNextAction() {
            super.onNextAction()
            startNextScreen()
        }
    }

    private val openCallback = object : AppOpenCallback() {
        override fun onNextAction() {
            super.onNextAction()
            startNextScreen()
        }
    }

    private fun startNextScreen(){
        launchActivity(LanguageStartActivity::class.java)
        finishAffinity()
    }

    override fun setViewBinding(): ActivitySplashBinding =
        ActivitySplashBinding.inflate(layoutInflater)

    override fun initViewModel(): Class<CommonVM> = CommonVM::class.java

    override fun initView() {
        if (!isTaskRoot
            && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
            && intent.action != null && intent.action.equals(Intent.ACTION_MAIN)
        ) {
            finish()
            return
        }
        logEvent(EventName.splash_open)
        if (NetworkUtil.isNetworkActive(this)){
            AsyncSplash.getInstance().init(
                activity = this,
                appOpenCallback = openCallback,
                interCallback = interCallback,
                adjustKey = getString(R.string.adjust_key),
                linkServer = getString(R.string.link_server),
                appId = getString(R.string.app_id),
                jsonIdAdsDefault = ""
            )
            AsyncSplash.getInstance().setDebug(true)
            AsyncSplash.getInstance().setListTurnOffRemoteKeys(TURN_OFF_CONFIGS)
            AsyncSplash.getInstance().setTimeOutSplash(12000)
            AsyncSplash.getInstance().setTimeOutCallApi(4000)
            AsyncSplash.getInstance().setInitResumeAdsNormal()
            AsyncSplash.getInstance().setUseDetectTestAd()
            AsyncSplash.getInstance().handleAsync(this, this, lifecycleScope) {
                Admob.getInstance().timeStart = AsyncSplash.getInstance().getTimeStartSplash()
                interCallback.onNextAction()
            }
            AsyncSplash.getInstance().setShowBannerSplash(
                true,
                binding.frBanner,
                arrayListOf("ca-app-pub-3571727899734025/4083604918"),
                BANNER_SPLASH,
            )
        } else {
            launchActivity(Bundle().apply {
                putString(SCREEN, SPLASH_ACTIVITY)
            },NoInternetActivity::class.java)
        }
    }

    override fun dataCollect() {

    }

    override fun onResume() {
        super.onResume()
        AsyncSplash.getInstance().checkShowSplashWhenFail()
    }

    override fun onBackPressedSystem() {

    }
}
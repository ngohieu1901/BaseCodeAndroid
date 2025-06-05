package com.hieunt.base.presentations.feature.screen_base.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.amazic.library.Utils.RemoteConfigHelper
import com.amazic.library.ads.admob.Admob
import com.amazic.library.ads.callback.AppOpenCallback
import com.amazic.library.ads.callback.InterCallback
import com.amazic.library.ads.splash_ads.AsyncSplash
import com.amazic.library.update_app.UpdateApplicationManager
import com.hieunt.base.R
import com.hieunt.base.base.BaseActivity
import com.hieunt.base.databinding.ActivitySplashBinding
import com.hieunt.base.di.IoDispatcher
import com.hieunt.base.firebase.ads.RemoteName
import com.hieunt.base.firebase.ads.RemoteName.BANNER_SPLASH
import com.hieunt.base.firebase.ads.RemoteName.TURN_OFF_CONFIGS
import com.hieunt.base.firebase.event.EventName
import com.hieunt.base.presentations.feature.screen_base.language_start.LanguageStartActivity
import com.hieunt.base.presentations.feature.screen_base.uninstall.ProblemActivity
import com.hieunt.base.presentations.feature.screen_base.welcome_back.WelcomeBackActivity
import com.hieunt.base.utils.SharePrefUtils
import com.hieunt.base.widget.launchActivity
import com.hieunt.base.widget.logEvent
import com.hieunt.base.widget.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    @Inject
    lateinit var sharePref: SharePrefUtils

    @Inject
    @IoDispatcher
    lateinit var ioDispatcher: CoroutineDispatcher

    companion object {
        var isOpenSplash = false
        var isOpenByAnotherApp = false
    }

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

    private fun startNextScreen() {
        launchActivity(LanguageStartActivity::class.java)
        finishAffinity()
    }

    override fun setViewBinding(): ActivitySplashBinding =
        ActivitySplashBinding.inflate(layoutInflater)

    @SuppressLint("SetTextI18n")
    override fun initView() {
        if (!isTaskRoot
            && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
            && intent.action != null && intent.action.equals(Intent.ACTION_MAIN)
        ) {
            finish()
            return
        }
        logEvent(EventName.splash_open)
        if (sharePref.countOpenApp <= 10) {
            isOpenSplash = true
            logEvent(EventName.splash_open + "_" + sharePref.countOpenApp)
        }

        if (Intent.ACTION_VIEW == intent.action) {
            if (intent.data != null) {
                //mở file từ app khác
                isOpenByAnotherApp = true
                handleAsync()
            } else {
                logEvent(EventName.uninstall_click)
                lifecycleScope.launch {
                    for (i in 1..100) {
                        binding.progressBar.progress = i
                        binding.tvProgress.text = getString(R.string.loading) + " ($i)%"
                        delay(30)
                    }
                    launchActivity(ProblemActivity::class.java)
                    finishAffinity()
                }
            }
        } else {
            lifecycleScope.launch {
                for (i in 1..100) {
                    binding.progressBar.progress = i
                    binding.tvProgress.text = getString(R.string.loading) + " ($i)%"
                    delay(30)
                }
            }

            UpdateApplicationManager.getInstance().init(this,
                object : UpdateApplicationManager.IonUpdateApplication {
                    override fun onUpdateApplicationFail() {
                        handleAsync()
                        toast("Update application fail")
                    }

                    override fun onUpdateApplicationSuccess() {
                        toast("Update application success")
                    }

                    override fun onMustNotUpdateApplication() {
                        handleAsync()
                    }

                    override fun requestUpdateFail() {
                        handleAsync()
                    }
                })

            RemoteConfigHelper.getInstance().fetchAllKeysAndTypes(this) {
                Admob.getInstance().showAllAds = RemoteConfigHelper.getInstance().get_config(this, RemoteConfigHelper.show_all_ads)
                Admob.getInstance().setTimeInterval(RemoteConfigHelper.getInstance().get_config_long(this, RemoteConfigHelper.interval_between_interstitial) * 1000)
                Admob.getInstance().setTimeIntervalFromStart(RemoteConfigHelper.getInstance().get_config_long(this, RemoteConfigHelper.interval_interstitial_from_start) * 1000)
                AsyncSplash.getInstance().setUseAppUpdateManager(true)

                if (RemoteConfigHelper.getInstance().get_config(this, "force_update_version")) {
                    UpdateApplicationManager.getInstance().checkVersionPlayStore(this, true, false,
                        "\uD83D\uDE80 New Update Available!",
                        "Upgrade now for a smoother experience, bug fixes for better performance. ⚡",
                        "Update Now",
                        "No")
                } else {
                    handleAsync()
                }
            }
        }
    }

    private fun handleAsync() {
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
        AsyncSplash.getInstance().setInitWelcomeBackAboveResumeAds(WelcomeBackActivity::class.java)
        AsyncSplash.getInstance().setUseDetectTestAd()
        AsyncSplash.getInstance().setOnPrepareLoadInterOpenSplashAds {
            if (isOpenByAnotherApp || (RemoteConfigHelper.getInstance().get_config(this@SplashActivity, RemoteName.TEST_FLOW) && sharePref.countOpenAppTestFlow >= 1)) {
                RemoteConfigHelper.getInstance().set_config(this@SplashActivity, RemoteName.OPEN_SPLASH, false)
                RemoteConfigHelper.getInstance().set_config(this@SplashActivity, RemoteName.INTER_SPLASH, false)
            }
        }
        AsyncSplash.getInstance()
            .handleAsync(
                this,
                this,
                lifecycleScope,
                onAsyncSplashDone = {
                    Admob.getInstance().setCustomAnimationDialog(true, R.raw.json_cat)
                },
                onNoInternetAction = {
                    Admob.getInstance().timeStart =
                        AsyncSplash.getInstance().getTimeStartSplash()
                    interCallback.onNextAction()
                })

        AsyncSplash.getInstance().setShowBannerSplash(
            true,
            binding.frBanner,
            arrayListOf("ca-app-pub-3571727899734025/4083604918"),
            BANNER_SPLASH,
        )
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
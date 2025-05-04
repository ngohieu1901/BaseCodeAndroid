package com.hieunt.base.ui.feature.screen_base.welcome_back

import com.amazic.library.ads.admob.Admob
import com.amazic.library.ads.admob.AdmobApi
import com.amazic.library.ads.app_open_ads.AppOpenManager
import com.amazic.library.ads.callback.AppOpenCallback
import com.amazic.library.ads.callback.BannerCallback
import com.hieunt.base.R
import com.hieunt.base.base.BaseActivity
import com.hieunt.base.databinding.ActivityWelcomeBackBinding
import com.hieunt.base.firebase.ads.RemoteName
import com.hieunt.base.firebase.ads.RemoteName.NATIVE_WB
import com.hieunt.base.ui.feature.container.ContainerActivity
import com.hieunt.base.widget.launchActivity
import com.hieunt.base.widget.tap
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeBackActivity : BaseActivity<ActivityWelcomeBackBinding>() {

    override fun initView() {
        loadNative(NATIVE_WB, R.layout.ads_shimmer_large_button_above, R.layout.ads_native_large_button_above)
        Admob.getInstance().loadBannerAds(
            this,
            AdmobApi.getInstance().getListIDByName(RemoteName.BANNER_SETTING),
            binding.bannerSetting,
            object : BannerCallback() {},
            {},
            RemoteName.BANNER_SETTING
        )

        binding.tvContinue.tap {
            loadAndShowResumeAds()
        }

    }

    private fun loadAndShowResumeAds() {
        AppOpenManager.getInstance().loadAndShowResumeAds(this, AdmobApi.getInstance().getListIDByName(RemoteName.RESUME_WB), object : AppOpenCallback() {
            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                launchActivity(ContainerActivity::class.java)
                finishAffinity()
            }

            override fun onAdFailedToShowFullScreenContent() {
                super.onAdFailedToShowFullScreenContent()
                launchActivity(ContainerActivity::class.java)
                finishAffinity()
            }

            override fun onAdFailedToLoad() {
                super.onAdFailedToLoad()
                launchActivity(ContainerActivity::class.java)
                finishAffinity()
            }
        }, RemoteName.RESUME_WB)
    }

    override fun dataCollect() {

    }

    override fun onBackPressedSystem() {
    }

    override fun setViewBinding(): ActivityWelcomeBackBinding = ActivityWelcomeBackBinding.inflate(layoutInflater)
}
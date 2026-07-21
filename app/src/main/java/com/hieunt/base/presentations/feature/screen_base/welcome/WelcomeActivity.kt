package com.hieunt.base.presentations.feature.screen_base.welcome

import com.hieunt.base.R
import com.hieunt.base.base.BaseActivity
import com.hieunt.base.databinding.ActivityWelcomeBinding
import com.hieunt.base.firebase.ads.RemoteName.NATIVE_PERMISSION
import com.hieunt.base.firebase.ads.activity.loadNative
import com.hieunt.base.presentations.feature.container.ContainerActivity
import com.hieunt.base.utils.SharePrefUtils
import com.hieunt.base.widget.launchActivity
import com.hieunt.base.widget.tap
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeActivity: BaseActivity<ActivityWelcomeBinding>(ActivityWelcomeBinding::inflate) {
    @Inject
    lateinit var sharePrefUtils: SharePrefUtils

    override fun initView() {
        loadNative(
            remoteKey = NATIVE_PERMISSION,
            remoteKeySecondary = NATIVE_PERMISSION,
            adsKeyMain = NATIVE_PERMISSION,
            adsKeySecondary = NATIVE_PERMISSION,
            idLayoutNative = R.layout.ads_native_large_button_above,
            idLayoutShimmer = R.layout.ads_shimmer_large_button_above
        )

        binding.tvContinue.tap {
            sharePrefUtils.isPassPermission = true
            launchActivity(ContainerActivity::class.java)
            finishAffinity()
        }
    }
}
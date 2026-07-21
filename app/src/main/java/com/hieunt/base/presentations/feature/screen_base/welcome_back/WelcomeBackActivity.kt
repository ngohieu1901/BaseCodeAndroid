package com.hieunt.base.presentations.feature.screen_base.welcome_back

import com.hieunt.base.R
import com.hieunt.base.base.BaseActivity
import com.hieunt.base.databinding.ActivityWelcomeBackBinding
import com.hieunt.base.firebase.ads.RemoteName.NATIVE_WB
import com.hieunt.base.firebase.ads.activity.loadNative
import com.hieunt.base.widget.tap
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeBackActivity :
    BaseActivity<ActivityWelcomeBackBinding>(ActivityWelcomeBackBinding::inflate) {

    override fun initView() {
        blockBackPress()

        loadNative(
            remoteKey = NATIVE_WB,
            remoteKeySecondary = NATIVE_WB,
            adsKeyMain = NATIVE_WB,
            adsKeySecondary = NATIVE_WB,
            idLayoutNative = R.layout.ads_native_large_button_above,
            idLayoutShimmer = R.layout.ads_shimmer_large_button_above
        )

        binding.tvContinue.tap {
            finish()
        }
    }
}
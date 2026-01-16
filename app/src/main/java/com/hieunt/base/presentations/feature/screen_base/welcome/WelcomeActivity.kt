package com.hieunt.base.presentations.feature.screen_base.welcome

import com.hieunt.base.base.BaseActivity
import com.hieunt.base.databinding.ActivityWelcomeBackBinding
import com.hieunt.base.presentations.feature.container.ContainerActivity
import com.hieunt.base.utils.SharePrefUtils
import com.hieunt.base.widget.launchActivity
import com.hieunt.base.widget.tap
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeActivity: BaseActivity<ActivityWelcomeBackBinding>(ActivityWelcomeBackBinding::inflate) {
    @Inject
    lateinit var sharePrefUtils: SharePrefUtils

    override fun initView() {
        binding.tvContinue.tap {
            sharePrefUtils.isPassPermission = true
            launchActivity(ContainerActivity::class.java)
            finishAffinity()
        }
    }

    override fun dataCollect() {

    }
}
package com.metaldetector.detectorapp.detectorapp.ui.feature.uninstall

import android.os.Bundle
import com.metaldetector.detectorapp.detectorapp.base.BaseActivity
import com.metaldetector.detectorapp.detectorapp.databinding.ActivityProblemBinding
import com.metaldetector.detectorapp.detectorapp.ui.feature.main.MainActivity
import com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.no_internet.NoInternetActivity
import com.metaldetector.detectorapp.detectorapp.utils.SystemUtils
import com.metaldetector.detectorapp.detectorapp.value.Default.IntentKeys.SCREEN
import com.metaldetector.detectorapp.detectorapp.value.Default.IntentKeys.SPLASH_ACTIVITY
import com.metaldetector.detectorapp.detectorapp.view_model.CommonVM
import com.metaldetector.detectorapp.detectorapp.widget.launchActivity
import com.metaldetector.detectorapp.detectorapp.widget.tap
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProblemActivity : BaseActivity<ActivityProblemBinding, CommonVM>() {
    override fun setViewBinding(): ActivityProblemBinding {
        return ActivityProblemBinding.inflate(layoutInflater)
    }

    override fun initViewModel(): Class<CommonVM> = CommonVM::class.java

    override fun initView() {
        binding.apply {
            listOf(tvExplore, tvTryAgain, noUninstall, ivBack).forEach {
                it.tap {
                    if (SystemUtils.haveNetworkConnection(this@ProblemActivity)){
                        launchActivity(MainActivity::class.java)
                        finishAffinity()
                    } else {
                        launchActivity(Bundle().apply {
                            putString(SCREEN, SPLASH_ACTIVITY)
                        }, NoInternetActivity::class.java)
                    }
                }
            }
            stillUninstall.tap {
                launchActivity(UninstallActivity::class.java)
            }
        }
    }

    override fun dataCollect() {

    }

}
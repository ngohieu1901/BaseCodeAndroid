package com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.metaldetector.detectorapp.detectorapp.R
import com.metaldetector.detectorapp.detectorapp.base.BaseActivity
import com.metaldetector.detectorapp.detectorapp.databinding.ActivitySplashBinding
import com.metaldetector.detectorapp.detectorapp.firebase.event.EventName
import com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.language_start.language_new.LanguageStartNewActivity
import com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.no_internet.NoInternetActivity
import com.metaldetector.detectorapp.detectorapp.ui.feature.uninstall.ProblemActivity
import com.metaldetector.detectorapp.detectorapp.utils.SystemUtils
import com.metaldetector.detectorapp.detectorapp.value.Default.IntentKeys.SCREEN
import com.metaldetector.detectorapp.detectorapp.value.Default.IntentKeys.SPLASH_ACTIVITY
import com.metaldetector.detectorapp.detectorapp.view_model.CommonVM
import com.metaldetector.detectorapp.detectorapp.widget.launchActivity
import com.metaldetector.detectorapp.detectorapp.widget.logEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding, CommonVM>() {

    private fun startNextScreen(){
        launchActivity(LanguageStartNewActivity::class.java)
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
        if (Intent.ACTION_VIEW == intent.action) {
            lifecycleScope.launch {
                for (i in 1..100) {
                    binding.progressBar.progress = i
                    binding.tvProgress.text = getString(R.string.loading) + " ($i)%"
                    delay(30)
                }
                launchActivity(ProblemActivity::class.java)
                finishAffinity()
            }
        } else {
            if (SystemUtils.haveNetworkConnection(this)){
                lifecycleScope.launch {
                    for (i in 1..100) {
                        binding.progressBar.progress = i
                        binding.tvProgress.text = getString(R.string.loading) + " ($i)%"
                        delay(30)
                    }
                    startNextScreen()
                }
            } else {
                launchActivity(Bundle().apply {
                    putString(SCREEN, SPLASH_ACTIVITY)
                },NoInternetActivity::class.java)
            }
        }
    }

    override fun dataCollect() {

    }
    override fun onBackPressedSystem() {

    }
}
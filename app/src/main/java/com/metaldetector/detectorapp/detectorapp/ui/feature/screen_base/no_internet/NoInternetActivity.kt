package com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.no_internet

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.OnBackPressedCallback
import com.metaldetector.detectorapp.detectorapp.base.BaseActivity
import com.metaldetector.detectorapp.detectorapp.databinding.ActivityNoInternetBinding
import com.metaldetector.detectorapp.detectorapp.firebase.ads.AdsHelper
import com.metaldetector.detectorapp.detectorapp.view_model.CommonVM
import com.metaldetector.detectorapp.detectorapp.widget.tap

class NoInternetActivity: BaseActivity<ActivityNoInternetBinding, CommonVM>() {
    override fun setViewBinding(): ActivityNoInternetBinding {
        return ActivityNoInternetBinding.inflate(layoutInflater)
    }

    override fun initViewModel(): Class<CommonVM> = CommonVM::class.java

    override fun initView() {
        val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        binding.tvTryAgain.tap {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val panelIntent = Intent(Settings.Panel.ACTION_WIFI)
                startActivity(panelIntent)
            } else {
                AdsHelper.disableResume(this)
                val wifiSettingsIntent = Intent(Settings.ACTION_WIFI_SETTINGS)
                startActivity(wifiSettingsIntent)
            }
        }
    }

    override fun dataCollect() {

    }

}
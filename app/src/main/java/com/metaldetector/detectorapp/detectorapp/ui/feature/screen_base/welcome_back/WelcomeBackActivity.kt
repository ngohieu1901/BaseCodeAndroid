package com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.welcome_back

import com.metaldetector.detectorapp.detectorapp.base.BaseActivity
import com.metaldetector.detectorapp.detectorapp.databinding.ActivityWelcomeBackBinding
import com.metaldetector.detectorapp.detectorapp.view_model.CommonVM
import com.metaldetector.detectorapp.detectorapp.widget.finishWithAnimation
import com.metaldetector.detectorapp.detectorapp.widget.tap
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeBackActivity : BaseActivity<ActivityWelcomeBackBinding, CommonVM>() {

    override fun initView() {
        enableButtonContinue()
    }

    override fun dataCollect() {

    }


    private fun enableButtonContinue() {
        binding.btnComeBack.alpha = 1f
        binding.btnComeBack.isEnabled = true
        binding.btnComeBack.tap {
            finishWithAnimation()
        }
    }

    override fun onBackPressedSystem() {
    }

    override fun setViewBinding(): ActivityWelcomeBackBinding = ActivityWelcomeBackBinding.inflate(layoutInflater)
    override fun initViewModel(): Class<CommonVM> = CommonVM::class.java

}
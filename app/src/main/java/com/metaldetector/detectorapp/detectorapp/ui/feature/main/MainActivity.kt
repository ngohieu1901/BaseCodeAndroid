package com.metaldetector.detectorapp.detectorapp.ui.feature.main

import com.metaldetector.detectorapp.detectorapp.utils.SharePrefUtils
import com.metaldetector.detectorapp.detectorapp.databinding.ActivityMainBinding
import com.metaldetector.detectorapp.detectorapp.base.BaseActivity
import com.metaldetector.detectorapp.detectorapp.view_model.CommonVM
import com.metaldetector.detectorapp.detectorapp.ui.dialog.RatingDialogFragment

private const val TAG = "MainActivity"

class MainActivity : BaseActivity<ActivityMainBinding, CommonVM>() {

    private val sharePrefUtils by lazy { SharePrefUtils(this) }

    override fun setViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun initViewModel(): Class<CommonVM> = CommonVM::class.java

    override fun initView() {

    }

    override fun dataCollect() {

    }

    override fun onBackPressedSystem() {
        if (!sharePrefUtils.isRated && arrayOf(1, 4, 6, 8).contains(sharePrefUtils.countExitApp)) {
            RatingDialogFragment(true, onClickRate = {

            }).show(supportFragmentManager, "RatingDialogExit")
        } else {
            if (!sharePrefUtils.isRated) {
                sharePrefUtils.countExitApp++
            }
            finishAffinity()
        }
    }
}
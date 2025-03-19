package com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.permission

import android.os.Build
import com.metaldetector.detectorapp.detectorapp.R
import com.metaldetector.detectorapp.detectorapp.base.BaseActivity
import com.metaldetector.detectorapp.detectorapp.databinding.ActivityPermissionBinding
import com.metaldetector.detectorapp.detectorapp.firebase.ads.RemoteName.NATIVE_PERMISSION
import com.metaldetector.detectorapp.detectorapp.firebase.event.EventName
import com.metaldetector.detectorapp.detectorapp.ui.dialog.WarningPermissionDialogFragment
import com.metaldetector.detectorapp.detectorapp.ui.feature.main.MainActivity
import com.metaldetector.detectorapp.detectorapp.utils.SharePrefUtils
import com.metaldetector.detectorapp.detectorapp.value.Default
import com.metaldetector.detectorapp.detectorapp.view_model.CommonVM
import com.metaldetector.detectorapp.detectorapp.widget.callMultiplePermissions
import com.metaldetector.detectorapp.detectorapp.widget.goToSetting
import com.metaldetector.detectorapp.detectorapp.widget.launchActivity
import com.metaldetector.detectorapp.detectorapp.widget.logEvent
import com.metaldetector.detectorapp.detectorapp.widget.tap

class PermissionActivity : BaseActivity<ActivityPermissionBinding, CommonVM>() {
    private val callLocationPermission = callMultiplePermissions {
        binding.ivSwitch.isChecked = it
    }
    private val listPermissionNeeded = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
        Default.LOCATION_PERMISSION
    else Default.LOCATION_PERMISSION.plus(Default.NOTIFICATION_PERMISSION)

    override fun setViewBinding(): ActivityPermissionBinding {
        return ActivityPermissionBinding.inflate(layoutInflater)
    }

    override fun initViewModel(): Class<CommonVM> = CommonVM::class.java

    override fun initView() {
        logEvent(EventName.permission_open)

        loadNative(NATIVE_PERMISSION, R.layout.ads_shimmer_permission, R.layout.ads_native_permission)

        binding.ivSwitch.setOnCheckedChangeListener { _, isChecked ->
            logEvent(EventName.permission_allow_click)
            binding.ivSwitch.isEnabled = !isChecked
        }
        binding.ivSwitch.tap {
            if (permissionUtils.canShowAllListPermissionDialogSystem(listPermissionNeeded)) {
                binding.ivSwitch.isChecked = false
                WarningPermissionDialogFragment {
                    goToSetting(this)
                }.show(supportFragmentManager, javaClass.name)
            } else {
                callLocationPermission.launch(listPermissionNeeded)
            }
        }
        binding.tvContinue.tap {
            logEvent(EventName.permission_continue_click)
            SharePrefUtils(this).isPassPermission = true
            launchActivity(MainActivity::class.java)
            finishAffinity()
        }
    }

    override fun dataCollect() {

    }

    override fun onResume() {
        super.onResume()
        binding.ivSwitch.isChecked = permissionUtils.isGrantMultiplePermissions(Default.LOCATION_PERMISSION)
    }

}
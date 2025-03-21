package com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.permission

import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.metaldetector.detectorapp.detectorapp.base.BaseActivity
import com.metaldetector.detectorapp.detectorapp.databinding.ActivityPermissionBinding
import com.metaldetector.detectorapp.detectorapp.firebase.event.EventName
import com.metaldetector.detectorapp.detectorapp.ui.dialog.WarningPermissionDialogFragment
import com.metaldetector.detectorapp.detectorapp.ui.feature.main.MainActivity
import com.metaldetector.detectorapp.detectorapp.value.Default
import com.metaldetector.detectorapp.detectorapp.view_model.CommonVM
import com.metaldetector.detectorapp.detectorapp.widget.callMultiplePermissions
import com.metaldetector.detectorapp.detectorapp.widget.goToSetting
import com.metaldetector.detectorapp.detectorapp.widget.gone
import com.metaldetector.detectorapp.detectorapp.widget.launchActivity
import com.metaldetector.detectorapp.detectorapp.widget.logEvent
import com.metaldetector.detectorapp.detectorapp.widget.tap
import com.metaldetector.detectorapp.detectorapp.widget.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PermissionActivity : BaseActivity<ActivityPermissionBinding, CommonVM>() {
    private val callMultiplePermission = callMultiplePermissions {
        binding.ivSwitch.isChecked = true
        binding.ivSwitch.isEnabled = false
    }

    private val listPermissionNeeded = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
        Default.STORAGE_PERMISSION
    else Default.STORAGE_PERMISSION.plus(Default.NOTIFICATION_PERMISSION)

    override fun setViewBinding(): ActivityPermissionBinding {
        return ActivityPermissionBinding.inflate(layoutInflater)
    }

    override fun initViewModel(): Class<CommonVM> = CommonVM::class.java

    override fun initView() {
        logEvent(EventName.permission_open)

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
                callMultiplePermission.launch(listPermissionNeeded)
            }
        }
        binding.ivSwitchOverlay.tap {
            if (!permissionUtils.isOverlayPermissionGranted()) {
                permissionUtils.requestOverlayPermission(this)
            }
        }
        binding.tvContinue.tap {
            sharePref.isPassPermission = true
            launchActivity(MainActivity::class.java)
            finishAffinity()
        }
        binding.tvSkip.tap {
            sharePref.isPassPermission = true
            launchActivity(MainActivity::class.java)
            finishAffinity()
        }
    }

    override fun dataCollect() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED){
                if (permissionUtils.isGrantMultiplePermissions(listPermissionNeeded)) {
                    binding.ivSwitch.isChecked = true
                }
                if (permissionUtils.isOverlayPermissionGranted()) {
                    binding.ivSwitchOverlay.isChecked = true
                }

                if (binding.ivSwitch.isChecked) {
                    binding.ivSwitch.isEnabled = false
                }
                if (binding.ivSwitchOverlay.isChecked) {
                    binding.ivSwitchOverlay.isEnabled = false
                }

                if (permissionUtils.isGrantMultiplePermissions(listPermissionNeeded) && permissionUtils.isOverlayPermissionGranted()) {
                    binding.tvContinue.visible()
                    binding.tvSkip.gone()
                } else {
                    binding.tvContinue.gone()
                    binding.tvSkip.visible()
                }
            }
        }
    }

}
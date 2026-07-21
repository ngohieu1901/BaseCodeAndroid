package com.hieunt.base.presentations.feature.screen_base.permission

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hieunt.base.R
import com.hieunt.base.base.BaseActivity
import com.hieunt.base.databinding.ActivityPermissionBinding
import com.hieunt.base.firebase.ads.RemoteName.NATIVE_PERMISSION
import com.hieunt.base.firebase.ads.activity.loadNative
import com.hieunt.base.firebase.event.EventName
import com.hieunt.base.presentations.feature.container.ContainerActivity
import com.hieunt.base.utils.SharePrefUtils
import com.hieunt.base.widget.callMultiplePermissions
import com.hieunt.base.widget.launchActivity
import com.hieunt.base.firebase.event.logEvent
import com.hieunt.base.widget.tap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PermissionActivity :
    BaseActivity<ActivityPermissionBinding>(ActivityPermissionBinding::inflate) {
    @Inject
    lateinit var sharePrefUtils: SharePrefUtils

    private val callStoragePermission = callMultiplePermissions {
        binding.ivSwitch.isChecked = true
        binding.ivSwitch.isEnabled = false
    }

    override fun initView() {
        logEvent(EventName.permission_open)
        loadNative(
            remoteKey = NATIVE_PERMISSION,
            remoteKeySecondary = NATIVE_PERMISSION,
            adsKeyMain = NATIVE_PERMISSION,
            adsKeySecondary = NATIVE_PERMISSION,
            idLayoutNative = R.layout.ads_native_large_button_above,
            idLayoutShimmer = R.layout.ads_shimmer_large_button_above
        )

        binding.ivSwitch.setOnCheckedChangeListener { _, isChecked ->
            logEvent(EventName.permission_allow_click)
            binding.ivSwitch.isEnabled = !isChecked
        }
        binding.ivSwitch.tap {

        }
        binding.tvContinue.tap {
            logEvent(EventName.permission_continue_click)
            sharePrefUtils.isPassPermission = true
            launchActivity(ContainerActivity::class.java)
            finishAffinity()
        }
    }

    override fun dataCollect() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                binding.ivSwitch.isChecked = true
                binding.ivSwitch.isEnabled = true
            }
        }
    }
}
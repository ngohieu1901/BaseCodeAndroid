package com.metaldetector.detectorapp.detectorapp.ui.feature.uninstall

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.lifecycleScope
import com.metaldetector.detectorapp.detectorapp.R
import com.metaldetector.detectorapp.detectorapp.base.BaseActivity
import com.metaldetector.detectorapp.detectorapp.databinding.ActivityUninstallBinding
import com.metaldetector.detectorapp.detectorapp.ui.feature.main.MainActivity
import com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.no_internet.NoInternetActivity
import com.metaldetector.detectorapp.detectorapp.ui_intent.UninstallUiIntent
import com.metaldetector.detectorapp.detectorapp.ui_state.UninstallUiState
import com.metaldetector.detectorapp.detectorapp.utils.SystemUtils
import com.metaldetector.detectorapp.detectorapp.value.Default.IntentKeys.SCREEN
import com.metaldetector.detectorapp.detectorapp.value.Default.IntentKeys.SPLASH_ACTIVITY
import com.metaldetector.detectorapp.detectorapp.widget.gone
import com.metaldetector.detectorapp.detectorapp.widget.launchActivity
import com.metaldetector.detectorapp.detectorapp.widget.tap
import com.metaldetector.detectorapp.detectorapp.widget.toast
import com.metaldetector.detectorapp.detectorapp.widget.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UninstallActivity : BaseActivity<ActivityUninstallBinding, UninstallViewModel>() {
    private lateinit var uninstallAdapter: UninstallAdapter
    override fun setViewBinding(): ActivityUninstallBinding {
        return ActivityUninstallBinding.inflate(layoutInflater)
    }

    override fun initViewModel(): Class<UninstallViewModel> {
        return UninstallViewModel::class.java
    }

    override fun initView() {
        viewModel.processIntent(UninstallUiIntent.Insert)

        binding.apply {
            uninstallAdapter = UninstallAdapter { data, pos ->
                viewModel.processIntent(UninstallUiIntent.Update(data))
                val lastAnswer = (viewModel.currentState as? UninstallUiState.Success)?.listAnswer?.size?.let { it - 1 }
                if (pos == lastAnswer) {
                    edAnswer.visible()
                } else {
                    edAnswer.gone()
                }
            }

            rvAnswer.adapter = uninstallAdapter

            tvUninstall.tap {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.parse("package:$packageName")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(intent)
            }
            tvCancel.tap {
                if (SystemUtils.haveNetworkConnection(this@UninstallActivity)){
                    launchActivity(MainActivity::class.java)
                    finishAffinity()
                } else {
                    launchActivity(Bundle().apply {
                        putString(SCREEN, SPLASH_ACTIVITY)
                    }, NoInternetActivity::class.java)
                }
            }
            ivBack.tap {
                finish()
            }
            edAnswer.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if (s != null && s.length > 30) {
                        toast(getString(R.string.maximum_30_characters))
                    }
                }
            })
        }
    }

    override fun dataCollect() {
        lifecycleScope.launch {
            viewModel.uiStore.collect {state ->
                when (state) {
                    is UninstallUiState.Idle -> {}

                    is UninstallUiState.Loading -> {
                        showLoading()
                    }

                    is UninstallUiState.Success -> {
                        dismissLoading()
                        uninstallAdapter.submitList(state.listAnswer)
                    }

                    is UninstallUiState.Error -> {
                        dismissLoading()
                        toast("Error: ${state.exception.message}")
                    }
                }
            }
        }
    }

}
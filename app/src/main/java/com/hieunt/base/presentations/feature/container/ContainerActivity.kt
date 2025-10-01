package com.hieunt.base.presentations.feature.container

import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.hieunt.base.R
import com.hieunt.base.base.BaseActivity
import com.hieunt.base.databinding.ActivityContainerBinding
import com.hieunt.base.firebase.event.EventName
import com.hieunt.base.presentations.components.dialogs.RatingDialogFragment
import com.hieunt.base.utils.SharePrefUtils
import com.hieunt.base.widget.logEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ContainerActivity : BaseActivity<ActivityContainerBinding>(ActivityContainerBinding::inflate) {
    @Inject
    lateinit var sharePref: SharePrefUtils

    companion object {
        var isOpenHome = false
    }

    override fun initView() {
        logEvent(EventName.home_open)
        if (sharePref.countOpenApp <= 10 && !isOpenHome) {
            isOpenHome = true
            logEvent(EventName.home_open + "_" + sharePref.countOpenApp)
        }
        sharePref.countOpenHome += 1
    }

    override fun onResume() {
        super.onResume()
        logEvent(EventName.home_view)
    }

    override fun dataCollect() {

    }
}
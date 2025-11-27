package com.hieunt.base.presentations.components.dialogs

import androidx.core.view.isVisible
import com.hieunt.base.base.BaseDialogFragment
import com.hieunt.base.databinding.DialogRewardBinding
import com.hieunt.base.widget.tap

class RewardDialog(
    private val isUseButtonRewardAll: Boolean = false,
    private val onWatchVideo: () -> Unit,
    private val onUnlockAll: () -> Unit
): BaseDialogFragment<DialogRewardBinding>(DialogRewardBinding::inflate) {
    override fun setupView() {
        binding.apply {
            llUnlockAll.isVisible = isUseButtonRewardAll

            llWatchVideo.tap {
                dismiss()
                onWatchVideo()
            }
            llUnlockAll.tap {
                dismiss()
                onUnlockAll()
            }
        }
    }

    override fun initData() {
    }

    override fun dataCollect() {
    }
}
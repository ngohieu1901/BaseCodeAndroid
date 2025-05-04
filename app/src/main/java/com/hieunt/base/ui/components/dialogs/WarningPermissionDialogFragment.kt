package com.hieunt.base.ui.components.dialogs

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hieunt.base.base.BaseDialogFragment
import com.hieunt.base.databinding.DialogWarningPermissionBinding
import com.hieunt.base.widget.tap

class WarningPermissionDialogFragment(private val goToSettingAction: () -> Unit) : BaseDialogFragment<DialogWarningPermissionBinding>(true) {

    override fun setViewBinding(inflater: LayoutInflater, container: ViewGroup?): DialogWarningPermissionBinding =
        DialogWarningPermissionBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.tvGoToSetting.tap {
            goToSettingAction.invoke()
            dismiss()
        }
    }

    override fun dataCollect() {

    }
}
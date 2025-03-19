package com.metaldetector.detectorapp.detectorapp.ui.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.metaldetector.detectorapp.detectorapp.base.BaseDialogFragment
import com.metaldetector.detectorapp.detectorapp.databinding.DialogWarningPermissionBinding
import com.metaldetector.detectorapp.detectorapp.view_model.CommonVM
import com.metaldetector.detectorapp.detectorapp.widget.tap

class WarningPermissionDialogFragment(private val goToSettingAction: () -> Unit) : BaseDialogFragment<DialogWarningPermissionBinding, CommonVM>(true) {

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

    override fun initViewModel(): Class<CommonVM> = CommonVM::class.java
}
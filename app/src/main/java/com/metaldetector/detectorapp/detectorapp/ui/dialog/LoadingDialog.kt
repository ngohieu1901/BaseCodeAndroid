package com.metaldetector.detectorapp.detectorapp.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import com.metaldetector.detectorapp.detectorapp.base.BaseDialog
import com.metaldetector.detectorapp.detectorapp.databinding.DialogLoadingBinding

class LoadingDialog(context: Context) : BaseDialog<DialogLoadingBinding>(context, false) {
    override fun initView() {

    }

    override fun initClickListener() {

    }

    override fun setViewBinding(
        inflater: LayoutInflater,
    ): DialogLoadingBinding {
        return DialogLoadingBinding.inflate(inflater)
    }
}
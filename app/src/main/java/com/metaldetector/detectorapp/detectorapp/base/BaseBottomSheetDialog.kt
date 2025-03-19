package com.metaldetector.detectorapp.detectorapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.metaldetector.detectorapp.detectorapp.R
import com.metaldetector.detectorapp.detectorapp.utils.PermissionUtils
import com.metaldetector.detectorapp.detectorapp.utils.SharePrefUtils
import com.metaldetector.detectorapp.detectorapp.utils.SystemUtils
import com.metaldetector.detectorapp.detectorapp.widget.hideNavigation
import javax.inject.Inject

abstract class BaseBottomSheetDialog<VB : ViewBinding>(private val isCancel: Boolean) : BottomSheetDialogFragment() {
    lateinit var binding: VB

    @Inject
    lateinit var permissionUtils: PermissionUtils

    @Inject
    lateinit var sharePref: SharePrefUtils

    protected abstract fun initView()
    protected abstract fun initClickListener()
    protected abstract fun setViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        SystemUtils.setLocale(activity)
        binding = setViewBinding(inflater, container)
        isCancelable = isCancel
        initView()
        initClickListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.hideNavigation()
    }

    override fun getTheme(): Int {
        return R.style.BaseBottomSheetDialog
    }

    override fun onDetach() {
        dialog?.dismiss()
        super.onDetach()
    }
}

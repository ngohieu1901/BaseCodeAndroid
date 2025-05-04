package com.hieunt.base.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hieunt.base.R
import com.hieunt.base.utils.PermissionUtils
import com.hieunt.base.utils.SharePrefUtils
import com.hieunt.base.utils.SystemUtils.setLocale
import com.hieunt.base.widget.hideNavigation
import javax.inject.Inject

abstract class BaseBottomSheetDialog<VB : ViewBinding>(private val isCancel: Boolean) : BottomSheetDialogFragment() {
    lateinit var binding: VB

    val permissionUtils by lazy { PermissionUtils(requireActivity()) }

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

    override fun onAttach(context: Context) {
        super.onAttach(setLocale(context))
    }
}

package com.hieunt.base.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.hieunt.base.R
import com.hieunt.base.utils.PermissionUtils
import com.hieunt.base.utils.SharePrefUtils
import com.hieunt.base.utils.SystemUtils.setLocale
import com.hieunt.base.widget.hideNavigation
import com.hieunt.base.widget.hideStatusBar

abstract class BaseDialogFragment<VB : ViewBinding>(private val isCancel: Boolean) : DialogFragment() {
    protected val permissionUtils by lazy { PermissionUtils(requireActivity())}
    protected val sharePref by lazy { SharePrefUtils(requireContext())}

    lateinit var binding: VB

    protected abstract fun initView()
    protected abstract fun dataCollect()
    protected abstract fun setViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = setViewBinding(inflater, container)
        isCancelable = isCancel
        initView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.hideNavigation()
        dialog?.window?.hideStatusBar()
        dataCollect()
    }

    override fun getTheme(): Int {
        return R.style.BaseDialog
    }

    override fun onDetach() {
        try {
            dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onDetach()
    }

    override fun onAttach(context: Context) {
        super.onAttach(setLocale(context))
    }
}
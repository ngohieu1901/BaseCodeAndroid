package com.metaldetector.detectorapp.detectorapp.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.metaldetector.detectorapp.detectorapp.R
import com.metaldetector.detectorapp.detectorapp.utils.PermissionUtils
import com.metaldetector.detectorapp.detectorapp.utils.SharePrefUtils
import com.metaldetector.detectorapp.detectorapp.utils.SystemUtils.setLocale
import com.metaldetector.detectorapp.detectorapp.widget.hideNavigation
import com.metaldetector.detectorapp.detectorapp.widget.hideStatusBar
import javax.inject.Inject

abstract class BaseDialogFragment<VB : ViewBinding, VM : ViewModel>(private val isCancel: Boolean) : DialogFragment() {
    protected val permissionUtils by lazy { PermissionUtils(requireActivity()) }

    protected val sharePref by lazy { SharePrefUtils(requireContext()) }

    lateinit var binding: VB
    lateinit var viewModel: VM

    protected abstract fun initView()
    protected abstract fun dataCollect()
    protected abstract fun initViewModel(): Class<VM>
    protected abstract fun setViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[initViewModel()]
    }

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
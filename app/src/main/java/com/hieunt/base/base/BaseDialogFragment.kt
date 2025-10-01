package com.hieunt.base.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.amazic.library.Utils.RemoteConfigHelper
import com.amazic.library.ads.admob.AdmobApi
import com.amazic.library.ads.native_ads.NativeBuilder
import com.amazic.library.ads.native_ads.NativeManager
import com.hieunt.base.R
import com.hieunt.base.utils.PermissionUtils
import com.hieunt.base.utils.SystemUtils.setLocale
import com.hieunt.base.widget.hideNavigation
import com.hieunt.base.widget.hideStatusBar

abstract class BaseDialogFragment<VB : ViewBinding>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB,
) : DialogFragment() {
    protected val permissionUtils by lazy { PermissionUtils(requireActivity()) }
    private var isCancelableCustom: Boolean = true

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected abstract fun setupView()
    protected abstract fun initData()
    protected abstract fun dataCollect()

    fun setCancelableCustom(cancelable: Boolean): BaseDialogFragment<VB> {
        this.isCancelableCustom = cancelable
        return this
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = isCancelableCustom
        initData()
        dataCollect()
    }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflate(
        inflater,
        container,
        false,
    ).also { _binding = it }.root

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.hideNavigation()
        dialog?.window?.hideStatusBar()
        setupView()
    }

    @CallSuper
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    @CallSuper
    override fun onDetach() {
        try {
            dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onDetach()
    }

    @CallSuper
    override fun onAttach(context: Context) {
        super.onAttach(setLocale(context))
    }

    override fun getTheme(): Int {
        return R.style.BaseDialog
    }

    protected fun loadNative(
        remoteKey: String,
        remoteKeySecondary: String,
        adsKeyMain: String,
        adsKeySecondary: String,
        idLayoutNative: Int,
        idLayoutShimmer: Int,
    ): NativeManager? {
        val frAds = binding.root.findViewById<FrameLayout>(R.id.fr_ads)
        if (frAds != null) {
            val nativeBuilder = NativeBuilder(requireContext(), frAds, idLayoutShimmer, idLayoutNative, idLayoutNative, true)
            nativeBuilder.setListIdAdMain(AdmobApi.getInstance().getListIDByName(adsKeyMain))
            nativeBuilder.setListIdAdSecondary(AdmobApi.getInstance().getListIDByName(adsKeySecondary))
            val nativeManager = NativeManager(requireContext(), viewLifecycleOwner, nativeBuilder, remoteKey, remoteKeySecondary)
            nativeManager.timeOutCallAds = 12000
            nativeManager.setIntervalReloadNative(
                RemoteConfigHelper.getInstance().get_config_long(requireContext(), RemoteConfigHelper.interval_reload_native) * 1000,
            )
            nativeManager.setAlwaysReloadOnResume(true)
            return nativeManager
        } else {
            return null
        }
    }
}
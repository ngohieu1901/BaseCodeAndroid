package com.hieunt.base.firebase.ads.fragment

import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.amazic.library.Utils.RemoteConfigHelper
import com.amazic.library.ads.admob.AdmobApi
import com.amazic.library.ads.native_ads.NativeBuilder
import com.amazic.library.ads.native_ads.NativeManager
import com.hieunt.base.R
import com.hieunt.base.firebase.ads.RemoteName

fun Fragment.loadNativeAll() {
    loadNative(
        RemoteName.NATIVE_ALL,
        RemoteName.NATIVE_ALL,
        RemoteName.NATIVE_ALL,
        RemoteName.NATIVE_ALL,
        R.layout.ads_native_large_button_above,
        R.layout.ads_shimmer_large_button_above
    )
}

fun Fragment.loadNative(
    remoteKey: String,
    remoteKeySecondary: String,
    adsKeyMain: String,
    adsKeySecondary: String,
    idLayoutNative: Int,
    idLayoutShimmer: Int,
): NativeManager? {
    val frAds = view?.findViewById<FrameLayout>(R.id.fr_ads)
    if (frAds != null) {
        val nativeBuilder = NativeBuilder(
            requireActivity().applicationContext,
            frAds,
            idLayoutShimmer,
            idLayoutNative,
            idLayoutNative,
            true
        )
        nativeBuilder.setListIdAdMain(AdmobApi.getInstance().getListIDByName(adsKeyMain))
        nativeBuilder.setListIdAdSecondary(
            AdmobApi.getInstance().getListIDByName(adsKeySecondary)
        )
        val nativeManager = NativeManager(
            requireActivity().applicationContext,
            viewLifecycleOwner,
            nativeBuilder,
            remoteKey,
            remoteKeySecondary
        )
        nativeManager.timeOutCallAds = 12000
        nativeManager.setIntervalReloadNative(
            RemoteConfigHelper.getInstance().get_config_long(
                requireContext(),
                RemoteConfigHelper.interval_reload_native
            ) * 1000,
        )
        nativeManager.setAlwaysReloadOnResume(true)
        return nativeManager
    } else {
        return null
    }
}
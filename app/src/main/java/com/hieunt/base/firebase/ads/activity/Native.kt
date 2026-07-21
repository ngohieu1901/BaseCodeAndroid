package com.hieunt.base.firebase.ads.activity

import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.amazic.library.Utils.RemoteConfigHelper
import com.amazic.library.ads.admob.AdmobApi
import com.amazic.library.ads.native_ads.NativeBuilder
import com.amazic.library.ads.native_ads.NativeManager
import com.hieunt.base.R
import com.hieunt.base.firebase.ads.RemoteName

fun AppCompatActivity.loadNativeAll() {
    loadNative(
        RemoteName.NATIVE_ALL,
        RemoteName.NATIVE_ALL,
        RemoteName.NATIVE_ALL,
        RemoteName.NATIVE_ALL,
        R.layout.ads_native_large_button_above,
        R.layout.ads_shimmer_large_button_above
    )
}

fun AppCompatActivity.loadNative(
    remoteKey: String,
    remoteKeySecondary: String,
    adsKeyMain: String,
    adsKeySecondary: String,
    idLayoutNative: Int,
    idLayoutShimmer: Int,
    idNativeMeta: Int = idLayoutNative,
    isAlwaysReloadOnResume: Boolean = true,
): NativeManager? {
    val frAds = findViewById<FrameLayout>(R.id.fr_ads)
    if (frAds != null) {
        val nativeBuilder =
            NativeBuilder(this, frAds, idLayoutShimmer, idLayoutNative, idNativeMeta, true)
        nativeBuilder.listIdAdMain = AdmobApi.getInstance().getListIDByName(adsKeyMain)
        nativeBuilder.listIdAdSecondary = AdmobApi.getInstance().getListIDByName(adsKeySecondary)
        val nativeManager =
            NativeManager(this, this, nativeBuilder, remoteKey, remoteKeySecondary)
        nativeManager.timeOutCallAds = 12000
        nativeManager.setIntervalReloadNative(
            RemoteConfigHelper.getInstance()
                .get_config_long(this, RemoteConfigHelper.interval_reload_native) * 1000,
        )
        nativeManager.setAlwaysReloadOnResume(isAlwaysReloadOnResume)
        return nativeManager
    }
    return null
}
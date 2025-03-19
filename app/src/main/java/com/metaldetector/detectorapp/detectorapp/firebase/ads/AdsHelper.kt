package com.metaldetector.detectorapp.detectorapp.firebase.ads

import android.app.Activity
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.amazic.library.ads.admob.Admob
import com.amazic.library.ads.app_open_ads.AppOpenManager
import com.amazic.library.ads.callback.NativeCallback
import com.amazic.library.ads.native_ads.NativeBuilder
import com.amazic.library.ads.native_ads.NativeManager
import com.google.android.gms.ads.nativead.NativeAd
import com.metaldetector.detectorapp.detectorapp.firebase.ads.RemoteName.APP_OPEN_RESUME
import com.metaldetector.detectorapp.detectorapp.widget.visible


object AdsHelper {
    fun turnOffAllAds() {
        Admob.getInstance().showAllAds = false
    }

    fun disableResume(activity: Activity) {
        AppOpenManager.getInstance().disableAppResumeWithActivity(activity.javaClass)
    }

    fun enableResume(activity: Activity) {
        if (Admob.getInstance().checkCondition(activity, APP_OPEN_RESUME)){
            AppOpenManager.getInstance().enableAppResumeWithActivity(activity.javaClass)
        }
    }

    fun loadNativeItem(
        activity: AppCompatActivity,
        frAds: FrameLayout,
        keyRemote: String,
        @LayoutRes idLayoutShimmer: Int,
        @LayoutRes idLayoutNative: Int,
        onNativeAdLoaded: () -> Unit
    ): NativeManager {
        frAds.visible()
        val nativeBuilder =
            NativeBuilder(activity, frAds, idLayoutShimmer, idLayoutNative, idLayoutNative)
        nativeBuilder.setListIdAd(keyRemote)
        return NativeManager(activity, activity, nativeBuilder, keyRemote).apply {
            setAlwaysReloadOnResume(true)
            object : NativeCallback() {
                override fun onNativeAdLoaded(nativeAd: NativeAd?) {
                    super.onNativeAdLoaded(nativeAd)
                    onNativeAdLoaded()
                }
            }
        }
    }

}
package com.hieunt.base.firebase.ads.fragment

import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.amazic.library.Utils.RemoteConfigHelper
import com.amazic.library.ads.admob.AdmobApi
import com.amazic.library.ads.banner_ads.BannerBuilder
import com.amazic.library.ads.banner_ads.BannerManager
import com.amazic.library.ads.collapse_banner_ads.CollapseBannerBuilder
import com.amazic.library.ads.collapse_banner_ads.CollapseBannerManager
import com.hieunt.base.R
import com.hieunt.base.firebase.ads.RemoteName

fun Fragment.loadBanner(adsKey: String) {
    val banner = view?.findViewById<FrameLayout>(R.id.fr_ads)
    if (banner != null) {
        val bannerBuilder = BannerBuilder(requireActivity(), banner, true)
        bannerBuilder.setListIdAdMain(AdmobApi.getInstance().getListIDByName(adsKey))
        val bannerManager =
            BannerManager(requireActivity(), viewLifecycleOwner, bannerBuilder, adsKey)
        bannerManager.setAlwaysReloadOnResume(true)
    }
}

fun Fragment.loadCollapseBanner(remoteKey: String): CollapseBannerManager? {
    val frContainerAds = view?.findViewById<FrameLayout>(R.id.fr_ads)
    if (frContainerAds != null) {
        val collapseBannerBuilder = CollapseBannerBuilder()
        collapseBannerBuilder.listId =
            AdmobApi.getInstance().getListIDByName(RemoteName.COLLAPSE_BANNER)
        val collapseBannerManager = CollapseBannerManager(
            requireActivity() as AppCompatActivity,
            frContainerAds,
            this,
            collapseBannerBuilder,
            remoteKey
        )
        collapseBannerManager.setIntervalReloadBanner(
            RemoteConfigHelper.getInstance().get_config_long(
                requireActivity().applicationContext,
                RemoteName.COLLAPSE_RELOAD_INTERVAL
            ) * 1000
        )
        collapseBannerManager.setAlwaysReloadOnResume(true)
        return collapseBannerManager
    }
    return null
}
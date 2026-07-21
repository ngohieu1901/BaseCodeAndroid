package com.hieunt.base.firebase.ads.activity

import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.amazic.library.Utils.RemoteConfigHelper
import com.amazic.library.ads.admob.AdmobApi
import com.amazic.library.ads.banner_ads.BannerBuilder
import com.amazic.library.ads.banner_ads.BannerManager
import com.amazic.library.ads.collapse_banner_ads.CollapseBannerBuilder
import com.amazic.library.ads.collapse_banner_ads.CollapseBannerManager
import com.hieunt.base.R
import com.hieunt.base.firebase.ads.RemoteName

fun AppCompatActivity.loadBannerSettings(): BannerManager? {
    val banner = findViewById<FrameLayout>(R.id.fr_banner_setting)
    if (banner != null) {
        val bannerBuilder = BannerBuilder(this, banner, true)
        bannerBuilder.setListIdAdMain(AdmobApi.getInstance().getListIDByName(RemoteName.BANNER_SETTING))
        val bannerManager = BannerManager(this, this, bannerBuilder, RemoteName.BANNER_SETTING)
        bannerManager.setAlwaysReloadOnResume(true)
        return bannerManager
    }
    return null
}

fun AppCompatActivity.loadBanner(adsKey: String): BannerManager? {
    val banner = findViewById<FrameLayout>(R.id.fr_ads)
    if (banner != null) {
        val bannerBuilder = BannerBuilder(this, banner, true)
        bannerBuilder.setListIdAdMain(AdmobApi.getInstance().getListIDByName(adsKey))
        val bannerManager = BannerManager(this, this, bannerBuilder, adsKey)
        bannerManager.setAlwaysReloadOnResume(true)
        return bannerManager
    }
    return null
}

fun AppCompatActivity.loadCollapseBanner(remoteKey: String): CollapseBannerManager?  {
    val frContainerAds = findViewById<FrameLayout>(R.id.fr_ads)
    if (frContainerAds != null) {
        val collapseBannerBuilder = CollapseBannerBuilder()
        collapseBannerBuilder.listId = AdmobApi.getInstance().getListIDByName(RemoteName.COLLAPSE_BANNER)
        val collapseBannerManager = CollapseBannerManager(this, frContainerAds, this, collapseBannerBuilder, remoteKey)
        collapseBannerManager.setIntervalReloadBanner(
            RemoteConfigHelper.getInstance().get_config_long(this, RemoteName.COLLAPSE_RELOAD_INTERVAL) * 1000
        )
        collapseBannerManager.setAlwaysReloadOnResume(true)
        return collapseBannerManager
    }
    return null
}
package com.hieunt.base.firebase.ads.activity

import androidx.appcompat.app.AppCompatActivity
import com.amazic.library.Utils.RemoteConfigHelper
import com.amazic.library.ads.admob.Admob
import com.amazic.library.ads.callback.InterCallback
import com.amazic.library.ads.inter_ads.InterManager
import com.amazic.library.organic.TechManager
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.hieunt.base.firebase.ads.RemoteName
import com.hieunt.base.firebase.ads.RemoteName.INTER_ALL

fun AppCompatActivity.loadAndShowInterAll(
    onNextAction: () -> Unit,
) {
    InterManager.loadAndShowInterAdsPreload(
        this,
        INTER_ALL,
        INTER_ALL,
        object : InterCallback() {
            override fun onNextAction() {
                super.onNextAction()
                onNextAction.invoke()
            }

            override fun onAdFailedToShowFullScreenContent() {
                super.onAdFailedToShowFullScreenContent()
                setIntervalInterAll(adsKey = INTER_ALL)
            }

            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                setIntervalInterAll(adsKey = INTER_ALL)
            }
        },
    )
}

fun AppCompatActivity.loadAndShowInter(
    adsKey: String,
    onNextAction: () -> Unit,
) {
    InterManager.loadAndShowInterAdsPreload(
        this,
        adsKey,
        adsKey,
        object : InterCallback() {
            override fun onNextAction() {
                super.onNextAction()
                onNextAction.invoke()
            }
        },
    )
}

fun AppCompatActivity.loadAndShowInter(
    adsKey: String,
    remoteKey: String,
    onNextAction: () -> Unit,
) {
    InterManager.loadAndShowInterAdsPreload(
        this,
        adsKey,
        remoteKey,
        object : InterCallback() {
            override fun onNextAction() {
                super.onNextAction()
                onNextAction.invoke()
            }
        },
    )
}

fun AppCompatActivity.setIntervalInterAll(adsKey: String) {
    val intervalInterAll = RemoteConfigHelper.getInstance().get_config_long(applicationContext, RemoteName.INTERVAL_INTER_ALL)
    if (adsKey == INTER_ALL && intervalInterAll > 0) {
        Admob.getInstance().setTimeInterval(intervalInterAll * 1000, false)
    }
}

fun AppCompatActivity.loadInterIntroAdsPreload() {
    InterManager.loadInterAdPreload(
        this,
        RemoteName.INTER_INTRO,
        RemoteName.INTER_INTRO,
    )
}

fun AppCompatActivity.showInterPreload(
    adsKey: String?,
    remoteKey: String?,
    onNextAction: () -> Unit,
    onLoaded: (() -> Unit)? = null,
    onFailed: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    onImpression: (() -> Unit)? = null,
) {
    InterManager.showInterAdPreload(
        this,
        adsKey,
        remoteKey,
        object : InterCallback() {
            override fun onNextAction() {
                super.onNextAction()
                onNextAction.invoke()
            }
            override fun onAdLoaded(interstitialAd: InterstitialAd?) {
                super.onAdLoaded(interstitialAd)
                onLoaded?.invoke()
            }

            override fun onAdFailedToShowFullScreenContent() {
                super.onAdFailedToShowFullScreenContent()
                onFailed?.invoke()
            }

            override fun onAdFailedToLoad() {
                super.onAdFailedToLoad()
                onFailed?.invoke()
            }

            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                onDismiss?.invoke()
            }

            override fun onAdImpression() {
                super.onAdImpression()
                onImpression?.invoke()
            }
        },
        true
    )
}

fun AppCompatActivity.showInterAdsSplash(onNextAction: () -> Unit) {
    if (Admob.getInstance().checkCondition(applicationContext, RemoteName.INTER_SPLASH) &&
        !TechManager.getInstance().isTech(applicationContext) &&
        Admob.getInstance().interstitialAdSplash != null
    ) {
        Admob.getInstance().showInterAds(
            this,
            Admob.getInstance().interstitialAdSplash,
            object : InterCallback() {
                override fun onNextAction() {
                    super.onNextAction()
                    onNextAction()
                }
            },
            false,
            RemoteName.INTER_SPLASH
        )
    } else {
        onNextAction()
    }
}


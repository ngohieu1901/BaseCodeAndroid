package com.hieunt.base.firebase.ads.fragment

import androidx.fragment.app.Fragment
import com.amazic.library.Utils.RemoteConfigHelper
import com.amazic.library.ads.admob.Admob
import com.amazic.library.ads.callback.InterCallback
import com.amazic.library.ads.inter_ads.InterManager
import com.hieunt.base.firebase.ads.RemoteName
import com.hieunt.base.firebase.ads.RemoteName.INTER_ALL

fun Fragment.loadAndShowInterAll(
    onNextAction: () -> Unit,
) {
    InterManager.loadAndShowInterAdsPreload(
        requireActivity(),
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

fun Fragment.loadAndShowInter(
    adsKey: String,
    onNextAction: () -> Unit,
) {
    InterManager.loadAndShowInterAdsPreload(
        requireActivity(),
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

fun Fragment.loadAndShowInter(
    adsKey: String,
    remoteKey: String,
    onNextAction: () -> Unit,
) {
    InterManager.loadAndShowInterAdsPreload(
        requireActivity(),
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

fun Fragment.setIntervalInterAll(adsKey: String) {
    val intervalInterAll = RemoteConfigHelper.getInstance().get_config_long(requireActivity().applicationContext, RemoteName.INTERVAL_INTER_ALL)
    if (adsKey == INTER_ALL && intervalInterAll > 0) {
        Admob.getInstance().setTimeInterval(intervalInterAll * 1000, false)
    }
}

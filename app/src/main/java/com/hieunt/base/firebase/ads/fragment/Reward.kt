package com.hieunt.base.firebase.ads.fragment

import androidx.fragment.app.Fragment
import com.amazic.library.ads.callback.RewardedCallback
import com.amazic.library.ads.reward_ads.RewardManager

fun Fragment.loadAndShowReward(
    adsKey: String,
    remoteKey: String,
    onNextAction: () -> Unit,
) {
    var earnedReward = false
    RewardManager.loadAndShowRewardAdsPreload(
        requireActivity(),
        adsKey,
        remoteKey,
        object : RewardedCallback() {
            override fun onUserEarnedReward() {
                super.onUserEarnedReward()
                earnedReward = true
            }

            override fun onNextAction() {
                super.onNextAction()
                if (earnedReward) {
                    onNextAction.invoke()
                }
            }
        }
    )
}

fun Fragment.loadAndShowReward(
    remoteKey: String,
    onNextAction: () -> Unit,
) {
    var earnedReward = false
    RewardManager.loadAndShowRewardAdsPreload(
        requireActivity(),
        remoteKey,
        remoteKey,
        object : RewardedCallback() {
            override fun onUserEarnedReward() {
                super.onUserEarnedReward()
                earnedReward = true
            }

            override fun onNextAction() {
                super.onNextAction()
                if (earnedReward) {
                    onNextAction.invoke()
                }
            }
        }
    )
}
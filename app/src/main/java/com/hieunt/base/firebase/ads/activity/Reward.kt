package com.hieunt.base.firebase.ads.activity

import androidx.appcompat.app.AppCompatActivity
import com.amazic.library.ads.callback.RewardedCallback
import com.amazic.library.ads.reward_ads.RewardManager

fun AppCompatActivity.loadAndShowReward(
    adsKey: String,
    remoteKey: String,
    onNextAction: () -> Unit,
) {
    var earnedReward = false
    RewardManager.loadAndShowRewardAdsPreload(
        this,
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

fun AppCompatActivity.loadAndShowReward(
    remoteKey: String,
    onNextAction: () -> Unit,
) {
    var earnedReward = false
    RewardManager.loadAndShowRewardAdsPreload(
        this,
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
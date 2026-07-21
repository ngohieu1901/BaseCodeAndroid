package com.hieunt.base.firebase.ads

import com.amazic.library.ads.admob.Admob

object AdsHelper {
    fun turnOffAllAds() {
        Admob.getInstance().showAllAds = false
    }
}
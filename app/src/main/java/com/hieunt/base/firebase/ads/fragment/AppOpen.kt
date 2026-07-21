package com.hieunt.base.firebase.ads.fragment

import androidx.fragment.app.Fragment
import com.amazic.library.ads.admob.Admob
import com.amazic.library.ads.app_open_ads.AppOpenManager
import com.hieunt.base.firebase.ads.RemoteName.RESUME_WB

fun Fragment.disableResume() {
    AppOpenManager.getInstance().disableAppResumeWithActivity(this.javaClass)
}

fun Fragment.enableResume() {
    if (Admob.getInstance().checkCondition(this.requireActivity().applicationContext, RESUME_WB)) {
        AppOpenManager.getInstance().enableAppResumeWithActivity(this.javaClass)
    }
}
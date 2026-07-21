package com.hieunt.base.firebase.ads.activity

import androidx.appcompat.app.AppCompatActivity
import com.amazic.library.ads.admob.Admob
import com.amazic.library.ads.app_open_ads.AppOpenManager
import com.hieunt.base.firebase.ads.RemoteName.RESUME_WB

fun AppCompatActivity.disableResume() {
    AppOpenManager.getInstance().disableAppResumeWithActivity(this.javaClass)
}

fun AppCompatActivity.enableResume() {
    if (Admob.getInstance().checkCondition(this.applicationContext, RESUME_WB)) {
        AppOpenManager.getInstance().enableAppResumeWithActivity(this.javaClass)
    }
}
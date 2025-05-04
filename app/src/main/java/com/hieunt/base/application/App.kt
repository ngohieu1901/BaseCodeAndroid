package com.hieunt.base.application

import com.amazic.library.application.AdsApplication
import com.hieunt.base.R
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : AdsApplication() {

    override fun getAppTokenAdjust(): String {
        return getString(R.string.adjust_key)
    }

    override fun getFacebookID(): String {
        return getString(R.string.facebook_id)
    }
}
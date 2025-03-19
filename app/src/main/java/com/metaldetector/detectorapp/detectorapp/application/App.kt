package com.metaldetector.detectorapp.detectorapp.application

import com.amazic.library.application.AdsApplication
import com.metaldetector.detectorapp.detectorapp.R

class App : AdsApplication() {

    override fun getAppTokenAdjust(): String {
        return getString(R.string.adjust_key)
    }

    override fun getFacebookID(): String {
        return getString(R.string.facebook_id)
    }

}
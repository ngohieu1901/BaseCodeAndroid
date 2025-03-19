package com.metaldetector.detectorapp.detectorapp.value

import android.Manifest
import android.content.res.Resources
import android.os.Build

object Default {
    const val PRIVACY_POLICY =
        "https://firebasestorage.googleapis.com/v0/b/asa120-metal-detector-v2.appspot.com/o/Privacy-Policy.html?alt=media&token=43273526-b33e-4c8a-8b5e-ac10aa2915cb"

    //Name permission
    val LOCATION_PERMISSION = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    const val BASE_URL = ""

    //Name permission
    val NOTIFICATION_PERMISSION = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        Manifest.permission.POST_NOTIFICATIONS
    else ""

    val STORAGE_PERMISSION = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) arrayOf(
        Manifest.permission.READ_MEDIA_VIDEO
    ) else arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    object IntentKeys {
        const val SCREEN = "SCREEN"
        const val SPLASH_ACTIVITY = "SplashActivity"
    }

    object Screen {
        val width: Int
            get() = Resources.getSystem().displayMetrics.widthPixels

        val height: Int
            get() = Resources.getSystem().displayMetrics.heightPixels
    }
}
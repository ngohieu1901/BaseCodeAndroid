package com.hieunt.base.widget

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.hieunt.base.firebase.ads.activity.disableResume

fun Context.goToSetting() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts(
        "package",
        applicationContext.packageName,
        null
    )
    intent.data = uri
    startActivity(intent)
}

fun Context.goToWifiSetting(activity: AppCompatActivity) {
    activity.disableResume()
    val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

package com.metaldetector.detectorapp.detectorapp.widget

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.metaldetector.detectorapp.detectorapp.firebase.ads.AdsHelper
import com.metaldetector.detectorapp.detectorapp.firebase.event.AdmobEvent

fun Context.goToSetting(activity: Activity) {
    AdsHelper.disableResume(activity)
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts(
        "package",
        applicationContext.packageName,
        null
    )
    intent.data = uri
    startActivity(intent)
}

fun Context.goToWifiSetting(activity: Activity) {
    AdsHelper.disableResume(activity)
    val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

fun Context.logEvent(nameEvent: String) {
    AdmobEvent.logEvent(this, nameEvent, Bundle())
}

fun Context.logEvent(nameEvent: String, bundle: Bundle) {
    AdmobEvent.logEvent(this, nameEvent, bundle)
}

fun Fragment.callMultiplePermissions(
    callbackPermission: (Boolean) -> Unit
): ActivityResultLauncher<Array<String>> {
    return registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { callback ->
        callbackPermission.invoke(!callback.containsValue(false))
    }
}

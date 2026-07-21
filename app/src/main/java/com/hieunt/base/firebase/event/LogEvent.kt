package com.hieunt.base.firebase.event

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics

fun Activity.logEvent(nameEvent: String, bundle: Bundle = Bundle()) {
    Log.i("LogEventTracking", "event: $nameEvent")
    FirebaseAnalytics.getInstance(this.applicationContext).logEvent(nameEvent, bundle)
}

fun Fragment.logEvent(nameEvent: String, bundle: Bundle = Bundle()) {
    Log.i("LogEventTracking", "event: $nameEvent")
    FirebaseAnalytics.getInstance(this.requireActivity().applicationContext).logEvent(nameEvent, bundle)
}
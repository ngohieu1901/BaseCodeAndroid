package com.hieunt.base.widget

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

//start activity
fun Fragment.launchActivity(
    clazz: Class<*>
) {
    startActivity(Intent(context, clazz))
}

fun Fragment.launchActivity(
    option: Bundle? = null,
    clazz: Class<*>
) {
    val intent = Intent(context, clazz)
    intent.putExtra("data_bundle", option)
    startActivity(intent)
}

fun Fragment.finishActivity() {
    activity?.finish()
}

fun Fragment.finishAffinity() {
    activity?.finishAffinity()
}

fun Fragment.currentBundle(): Bundle? {
    return activity?.intent?.getBundleExtra("data_bundle")
}

fun Fragment.toast(msg: String?) {
    if (msg == null) return
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}



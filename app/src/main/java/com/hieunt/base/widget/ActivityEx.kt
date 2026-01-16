package com.hieunt.base.widget

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hieunt.base.R
import com.hieunt.base.firebase.event.AdmobEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

//get multiple permissions
fun AppCompatActivity.callMultiplePermissions(
    callbackPermission: (Boolean) -> Unit
): ActivityResultLauncher<Array<String>> {
    return registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { callback ->
        callbackPermission.invoke(!callback.containsValue(false))
    }
}

//start activity
fun AppCompatActivity.launchActivity(
    clazz: Class<*>
) {
    val intent = Intent(this, clazz)
    val option = Bundle()
    option.putString("last_activity", clazz.name)
    intent.putExtra("data_bundle", option)
    startActivity(intent)
}

fun AppCompatActivity.launchActivity(
    option: Bundle,
    clazz: Class<*>
) {
    option.putString("last_activity", clazz.name)
    val intent = Intent(this, clazz)
    intent.putExtra("data_bundle", option)
    startActivity(intent)
}

fun AppCompatActivity.currentBundle(): Bundle? {
    return intent.getBundleExtra("data_bundle")
}

inline fun <reified T : Parcelable> AppCompatActivity.currentParcelable(key: String): T? {
    val bundle = currentBundle()
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        bundle?.getParcelable(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        bundle?.getParcelable(key)
    }
}

fun AppCompatActivity.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.callPermissions(
    callbackPermission: (Boolean) -> Unit
): ActivityResultLauncher<String> {
    return registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { callback ->
        callbackPermission.invoke(callback)
    }
}

fun LifecycleOwner.launchAndRepeatWhenStarted(
    launchBlock: suspend () -> Unit,
    vararg launchBlocks: suspend () -> Unit,
): Job =
    lifecycleScope.launch {
        repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            launch { launchBlock() }
            launchBlocks.forEach { launch { it() } }
        }
    }

fun Activity.logEvent(nameEvent: String, bundle: Bundle = Bundle()) {
    AdmobEvent.logEvent(this, nameEvent, bundle)
}
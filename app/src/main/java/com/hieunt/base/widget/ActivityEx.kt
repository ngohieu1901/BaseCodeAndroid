package com.hieunt.base.widget

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

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
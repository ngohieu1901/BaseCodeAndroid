package com.hieunt.base.widget

import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

fun AppCompatActivity.callPermission(
    callbackPermission: (Boolean) -> Unit
): ActivityResultLauncher<String> {
    return registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { callback ->
        callbackPermission.invoke(callback)
    }
}

fun AppCompatActivity.callMultiplePermissions(
    callbackPermission: (Boolean) -> Unit
): ActivityResultLauncher<Array<String>> {
    return registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { callback ->
        callbackPermission.invoke(!callback.containsValue(false))
    }
}

fun AppCompatActivity.hasPermission(permission: String): Boolean {
    return ActivityCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

fun AppCompatActivity.hasAllPermissions(permissions: Array<String>): Boolean {
    return permissions.all { hasPermission(it) }
}

fun AppCompatActivity.shouldShowRationale(permission: String): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
}

fun AppCompatActivity.shouldShowRationale(permissions: Array<String>): Boolean {
    return permissions.any { shouldShowRationale(it) }
}

fun AppCompatActivity.isPermissionPermanentlyDenied(permission: String): Boolean {
    return !hasPermission(permission) && !shouldShowRationale(permission)
}
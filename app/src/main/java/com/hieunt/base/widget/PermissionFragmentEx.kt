package com.hieunt.base.widget

import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

fun Fragment.callMultiplePermissions(
    callbackPermission: (Boolean) -> Unit
): ActivityResultLauncher<Array<String>> {
    return registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { callback ->
        callbackPermission.invoke(!callback.containsValue(false))
    }
}

fun Fragment.callPermissions(
    callbackPermission: (Boolean) -> Unit
): ActivityResultLauncher<String> {
    return registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { callback ->
        callbackPermission.invoke(callback)
    }
}

fun Fragment.hasPermission(permission: String): Boolean {
    return ActivityCompat.checkSelfPermission(
        requireContext(),
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

fun Fragment.hasAllPermissions(permissions: Array<String>): Boolean {
    return permissions.all { hasPermission(it) }
}

fun Fragment.shouldShowRationale(permission: String): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)
}

fun Fragment.shouldShowRationale(permissions: Array<String>): Boolean {
    return permissions.any { shouldShowRationale(it) }
}
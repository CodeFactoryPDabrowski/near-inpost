package com.codefactory.przemyslawdabrowski.nearinpost.extension

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.inputmethod.InputMethodManager
import com.codefactory.przemyslawdabrowski.nearinpost.view.base.BaseActivity
import com.codefactory.przemyslawdabrowski.nearinpost.view.base.BaseFragment

/**
 * Hide keyboard for for specific view token.
 * @param windowToken Token of view which demands to show soft keyboard.
 */
fun BaseFragment.hideKeyboard(windowToken: IBinder) {
    if (activity != null) {
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
}

/**
 * Check if permissions are given.
 * @param permissions Permissions to check.
 * @param requestCode Request code of permission check.
 * @param permissionsGranted Block execute if permissions are granted.
 */
inline fun BaseFragment.givenPermission(permissions: Array<String>, requestCode: Int, crossinline permissionsGranted: () -> Unit) {
    if (activity == null) {
        return
    }
    if (activity !is BaseActivity) {
        throw IllegalArgumentException("Application should use only BaseActivity class, error class: $activity")
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (arePermissionsGranted(activity as BaseActivity, permissions)) {
            permissionsGranted()
        } else {
            askForPermissions(activity as BaseActivity, permissions, requestCode)
        }
    } else {
        permissionsGranted()
    }
}

/**
 * Ask for passed permissions.
 * @param activity Activity needed for request.
 * @param permissions Permissions to ask for.
 * @param requestCode Code for result.
 */
fun askForPermissions(activity: BaseActivity, permissions: Array<String>, requestCode: Int) {
    ActivityCompat.requestPermissions(activity, permissions, requestCode)
}

/**
 * Check if permissions are granted.
 * @param activity Activity needed to check if permissions are granted.
 * @param permissions Permissions to check.
 *
 * @return Value determinate if permissions are granted.
 */
fun arePermissionsGranted(activity: BaseActivity, permissions: Array<String>): Boolean {
    return permissions.any { isPermissionGranted(activity, it) }
}

/**
 * Check if single permission is granted.
 * @param activity Activity needed to check if permissions are granted.
 * @param permission Permission to check.
 *
 * @return Value determinate if permission is granted.
 */
fun isPermissionGranted(activity: BaseActivity, permission: String): Boolean {
    return (ContextCompat.checkSelfPermission(activity, permission)) == PackageManager.PERMISSION_GRANTED
}

package com.codefactory.przemyslawdabrowski.nearinpost.extension

import android.content.Context
import android.os.IBinder
import android.view.inputmethod.InputMethodManager
import com.codefactory.przemyslawdabrowski.nearinpost.view.base.BaseFragment

/**
 * Hide keyboard for for specific view token.
 * @param windowToken Token of view which demands to show soft keyboard.
 */
fun BaseFragment.hideKeyboard(windowToken: IBinder) {
    if (activity != null) {
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
    }
}

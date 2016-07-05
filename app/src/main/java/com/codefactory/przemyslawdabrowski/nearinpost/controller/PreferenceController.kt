package com.codefactory.przemyslawdabrowski.nearinpost.controller

import android.content.Context
import android.content.SharedPreferences
import com.codefactory.przemyslawdabrowski.nearinpost.app.App
import com.codefactory.przemyslawdabrowski.nearinpost.injection.scope.AppScope
import javax.inject.Inject

@AppScope
class PreferenceController @Inject constructor(val app: App) {

    /**
     * Name of shared preferences.
     */
    private val SHARED_PREFERENCES_NAME = "com.codefactory.przemyslaw.dabrowski.nearinpost.shared.preferences"

    /**
     * Key for max results of searching in post machines.
     */
    private val SHARED_PREFERENCES_MAX_RESULTS_KEY = "max.inpost.machines.results.key"

    /**
     * shared preferences instance per app.
     */
    private val preferences: SharedPreferences

    init {
        preferences = app.applicationContext.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    var maxInPostMachinesResult: Int
        get() {
            return preferences.getInt(SHARED_PREFERENCES_MAX_RESULTS_KEY, 3)
        }
        set(value) {
            preferences.edit().putInt(SHARED_PREFERENCES_MAX_RESULTS_KEY, value).apply()
        }
}
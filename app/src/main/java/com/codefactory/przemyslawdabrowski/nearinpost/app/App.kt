package com.codefactory.przemyslawdabrowski.nearinpost.app

import android.app.Application
import com.codefactory.przemyslawdabrowski.nearinpost.injection.component.AppComponent
import com.codefactory.przemyslawdabrowski.nearinpost.injection.component.DaggerAppComponent
import com.codefactory.przemyslawdabrowski.nearinpost.injection.module.ApiModule
import com.codefactory.przemyslawdabrowski.nearinpost.injection.module.AppModule
import com.codefactory.przemyslawdabrowski.nearinpost.injection.module.DatabaseModule
import com.codefactory.przemyslawdabrowski.nearinpost.injection.module.LocationModule
import com.facebook.stetho.BuildConfig
import com.facebook.stetho.Stetho

class App : Application() {

    companion object {

        /**
         * Dagger Application component.
         */
        @JvmStatic lateinit var appComponent: AppComponent

        /**
         * Base URL to communicate with inPost API.
         */
        val PACZKOMATY_BASE_URL: String = "http://api.paczkomaty.pl"

        val DATABASE_NAME = "com.codefactory.przemyslaw.dabrowski.nearinpost.database"
        val DATABASE_SCHEMA_VERSION = 1L
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
        initComponent()
    }

    /**
     *Initialize application scoped component.
     */
    private fun initComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .apiModule(ApiModule(PACZKOMATY_BASE_URL))
                .locationModule(LocationModule())
                .databaseModule(DatabaseModule(DATABASE_NAME, DATABASE_SCHEMA_VERSION))
                .build()
        appComponent.inject(this)
    }
}

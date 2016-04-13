package com.codefactory.przemyslawdabrowski.nearinpost.app

import android.app.Application
import com.codefactory.przemyslawdabrowski.nearinpost.injection.component.AppComponent
import com.codefactory.przemyslawdabrowski.nearinpost.injection.component.DaggerAppComponent
import com.codefactory.przemyslawdabrowski.nearinpost.injection.module.ApiModule
import com.codefactory.przemyslawdabrowski.nearinpost.injection.module.AppModule
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
    }

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        initComponent()
    }

    /**
     *Initialize application scoped component.
     */
    private fun initComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .apiModule(ApiModule(PACZKOMATY_BASE_URL))
                .build()
        appComponent.inject(this)
    }
}

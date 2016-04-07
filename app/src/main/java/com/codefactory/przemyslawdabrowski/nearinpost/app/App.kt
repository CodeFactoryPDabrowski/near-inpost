package com.codefactory.przemyslawdabrowski.nearinpost.app

import android.app.Application
import com.codefactory.przemyslawdabrowski.nearinpost.injection.component.AppComponent
import com.codefactory.przemyslawdabrowski.nearinpost.injection.component.DaggerAppComponent
import com.codefactory.przemyslawdabrowski.nearinpost.injection.module.AppModule
import com.facebook.stetho.Stetho

class App : Application() {

    companion object {
        @JvmStatic lateinit var appComponent: AppComponent
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
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        appComponent.inject(this)
    }
}

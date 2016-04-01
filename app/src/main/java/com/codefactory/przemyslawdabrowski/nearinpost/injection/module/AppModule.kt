package com.codefactory.przemyslawdabrowski.nearinpost.injection.module

import com.codefactory.przemyslawdabrowski.nearinpost.app.App
import com.codefactory.przemyslawdabrowski.nearinpost.injection.scope.AppScope
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val application: App) {

    /**
     * Instance of {@link App} for dagger injection.
     */
    val app: App = application

    @AppScope
    @Provides
    fun provideApp() = app
}
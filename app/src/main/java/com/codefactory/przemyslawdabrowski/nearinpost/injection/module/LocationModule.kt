package com.codefactory.przemyslawdabrowski.nearinpost.injection.module

import com.codefactory.przemyslawdabrowski.nearinpost.app.App
import com.codefactory.przemyslawdabrowski.nearinpost.injection.scope.AppScope
import dagger.Module
import dagger.Provides
import pl.charmas.android.reactivelocation.ReactiveLocationProvider

@Module
class LocationModule {

    @AppScope
    @Provides
    fun provideReactiveLocation(app: App) = ReactiveLocationProvider(app)
}
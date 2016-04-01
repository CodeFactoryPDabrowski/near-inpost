package com.codefactory.przemyslawdabrowski.nearinpost.injection.component

import com.codefactory.przemyslawdabrowski.nearinpost.app.App
import com.codefactory.przemyslawdabrowski.nearinpost.injection.module.ApiModule
import com.codefactory.przemyslawdabrowski.nearinpost.injection.module.AppModule
import com.codefactory.przemyslawdabrowski.nearinpost.injection.scope.AppScope
import dagger.Component

@AppScope
@Component(modules = arrayOf(AppModule::class, ApiModule::class))
interface AppComponent {

    /**
     * Inject into {@link App} AppComponent dependencies.
     */
    fun inject(app: App)
}

package com.codefactory.przemyslawdabrowski.nearinpost.injection.component

import com.codefactory.przemyslawdabrowski.nearinpost.app.App
import com.codefactory.przemyslawdabrowski.nearinpost.controller.PreferenceController
import com.codefactory.przemyslawdabrowski.nearinpost.data.MachineRepository
import com.codefactory.przemyslawdabrowski.nearinpost.injection.module.ApiModule
import com.codefactory.przemyslawdabrowski.nearinpost.injection.module.AppModule
import com.codefactory.przemyslawdabrowski.nearinpost.injection.module.DatabaseModule
import com.codefactory.przemyslawdabrowski.nearinpost.injection.module.LocationModule
import com.codefactory.przemyslawdabrowski.nearinpost.injection.scope.AppScope
import com.codefactory.przemyslawdabrowski.nearinpost.navigation.Navigator
import dagger.Component
import io.realm.Realm
import pl.charmas.android.reactivelocation.ReactiveLocationProvider
import retrofit2.Retrofit

@AppScope
@Component(modules = arrayOf(AppModule::class, ApiModule::class, LocationModule::class, DatabaseModule::class))
interface AppComponent {

    /**
     * Inject into {@link App} AppComponent dependencies.
     */
    fun inject(app: App)

    /**
     * @return Retrofit singleton object.
     */
    fun retrofit(): Retrofit

    /**
     * Provide location provider.
     */
    fun locationProvider(): ReactiveLocationProvider

    /**
     * Provide navigator object for navigation between activities.
     */
    fun navigator(): Navigator

    /**
     * Provide mechanism to save and obtain shared preferences.
     */
    fun prefs(): PreferenceController

    /**
     * Provide Realm Database.
     */
    fun realmDB(): Realm

    /**
     * Provide machine repository.
     */
    fun machineRepository(): MachineRepository
}

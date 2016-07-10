package com.codefactory.przemyslawdabrowski.nearinpost.injection.module

import com.codefactory.przemyslawdabrowski.nearinpost.app.App
import com.codefactory.przemyslawdabrowski.nearinpost.injection.scope.AppScope
import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration

@Module
class DatabaseModule(val dbName: String, val schemaVersion: Long) {

    @AppScope
    @Provides
    fun provideRealmConfiguration(app: App): RealmConfiguration = RealmConfiguration.
            Builder(app).name(dbName).schemaVersion(schemaVersion).build()

    //TODO: Check if realm fragment scope instance isn't better??? realm.close in onDestroy?
    @AppScope
    @Provides
    fun provideDB(configuration: RealmConfiguration): Realm = Realm.getInstance(configuration)
}

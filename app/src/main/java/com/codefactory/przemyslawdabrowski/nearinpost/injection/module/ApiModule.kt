package com.codefactory.przemyslawdabrowski.nearinpost.injection.module

import com.codefactory.przemyslawdabrowski.nearinpost.injection.scope.AppScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

@Module
class ApiModule() {

    companion object {
        val PACZKOMATY_BASE_URL: String = "http://api.paczkomaty.pl"
    }

    @AppScope
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(PACZKOMATY_BASE_URL)
                .client(OkHttpClient())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()
    }
}

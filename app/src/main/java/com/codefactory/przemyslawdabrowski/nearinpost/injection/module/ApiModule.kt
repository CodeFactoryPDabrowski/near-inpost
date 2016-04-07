package com.codefactory.przemyslawdabrowski.nearinpost.injection.module

import com.codefactory.przemyslawdabrowski.nearinpost.injection.scope.AppScope
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

@Module
class ApiModule() {

    companion object {
        val PACZKOMATY_BASE_URL: String = "http://api.paczkomaty.pl"
    }

    @AppScope
    @Provides
    fun provideOkHTTPClient(): OkHttpClient {
        val client = OkHttpClient.Builder().addNetworkInterceptor(StethoInterceptor()).build()

        return client
    }

    @AppScope
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(PACZKOMATY_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
    }
}

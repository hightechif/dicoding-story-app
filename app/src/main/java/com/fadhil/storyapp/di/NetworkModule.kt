package com.fadhil.storyapp.di

import android.content.SharedPreferences
import com.fadhil.storyapp.data.source.local.prefs.HttpHeaderLocalSource
import com.fadhil.storyapp.data.source.remote.network.AuthInterceptor
import com.fadhil.storyapp.data.source.remote.network.StoryApi
import com.fadhil.storyapp.data.source.remote.network.StoryApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpHeaderLocalSource(
        sharedPreferences: SharedPreferences
    ) = HttpHeaderLocalSource(sharedPreferences)

    @Singleton
    @Provides
    fun provideLoggerInterceptor(): HttpLoggingInterceptor = StoryApi.buildLoggerInterceptor()

    @Singleton
    @Provides
    fun provideAuthInterceptor(httpHeaderLocalSource: HttpHeaderLocalSource): AuthInterceptor =
        StoryApi.buildAuthInterceptor(httpHeaderLocalSource)

    @Singleton
    @Provides
    fun provideHttpClient(
        loggerInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient = StoryApi.buildClient(loggerInterceptor, authInterceptor)

    @Singleton
    @Provides
    fun provideRetrofit(
        httpClient: OkHttpClient
    ): Retrofit = StoryApi.buildRetrofit(httpClient)

}
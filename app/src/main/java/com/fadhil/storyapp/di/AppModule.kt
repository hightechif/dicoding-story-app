package com.fadhil.storyapp.di

import com.fadhil.storyapp.data.source.remote.network.StoryApi
import com.fadhil.storyapp.data.source.remote.network.StoryApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideStoryApiService(retrofit: Retrofit): StoryApiService = StoryApi.buildApiService(retrofit)

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

}
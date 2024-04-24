package com.fadhil.storyapp.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.room.Room
import com.fadhil.storyapp.data.source.local.db.AppDatabase
import com.fadhil.storyapp.data.source.local.prefs.SettingPreferences
import com.fadhil.storyapp.data.source.local.prefs.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DATABASE_NAME = "AppDB"

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(
            appContext.applicationContext, AppDatabase::class.java, DATABASE_NAME
        ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(appContext)

    @Singleton
    @Provides
    fun provideSettingPreferences(@ApplicationContext appContext: Context): SettingPreferences =
        SettingPreferences.getInstance(appContext.dataStore)

    @Singleton
    @Provides
    fun provideStoryDao(db: AppDatabase) = db.storyDao()

}
package com.fadhil.storyapp.di

import com.fadhil.storyapp.data.source.AuthRepository
import com.fadhil.storyapp.data.source.ConfigurationRepository
import com.fadhil.storyapp.data.source.SettingRepository
import com.fadhil.storyapp.domain.repository.IStoryRepository
import com.fadhil.storyapp.domain.usecase.AuthUseCase
import com.fadhil.storyapp.domain.usecase.ConfigurationUseCase
import com.fadhil.storyapp.domain.usecase.SettingUseCase
import com.fadhil.storyapp.domain.usecase.StoryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideConfigurationUseCase(
        configurationRepository: ConfigurationRepository
    ) = ConfigurationUseCase(configurationRepository)

    @Singleton
    @Provides
    fun provideSettingUseCase(
        settingRepository: SettingRepository
    ) = SettingUseCase(settingRepository)

    @Singleton
    @Provides
    fun provideAuthUseCase(
        authRepository: AuthRepository
    ) = AuthUseCase(authRepository)

    @Singleton
    @Provides
    fun provideStoryUseCase(
        storyRepository: IStoryRepository
    ) = StoryUseCase.getInstance(storyRepository)

}
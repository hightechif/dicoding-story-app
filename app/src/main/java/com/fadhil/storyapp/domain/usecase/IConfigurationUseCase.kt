package com.fadhil.storyapp.domain.usecase

import com.fadhil.storyapp.domain.model.Configuration
import com.fadhil.storyapp.domain.model.Session
import kotlinx.coroutines.flow.Flow

interface IConfigurationUseCase {
    fun get(): Flow<Configuration?>
    fun update(session: Session, email: String): Flow<Configuration>
}
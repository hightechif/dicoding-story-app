package com.fadhil.storyapp.domain.repository

import com.fadhil.storyapp.domain.model.Configuration
import com.fadhil.storyapp.domain.model.Session
import kotlinx.coroutines.flow.Flow

interface IConfigurationRepository {
    fun get(): Flow<Configuration?>
    fun update(session: Session, username: String): Flow<Configuration>
}
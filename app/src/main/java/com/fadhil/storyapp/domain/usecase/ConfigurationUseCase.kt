package com.fadhil.storyapp.domain.usecase

import com.fadhil.storyapp.domain.model.Configuration
import com.fadhil.storyapp.domain.model.Session
import com.fadhil.storyapp.domain.repository.IConfigurationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConfigurationUseCase @Inject constructor(
    private val repository: IConfigurationRepository
) : IConfigurationUseCase {

    override fun get(): Flow<Configuration?> = repository.get()

    override fun update(session: Session, email: String): Flow<Configuration> =
        repository.update(session, email)

}
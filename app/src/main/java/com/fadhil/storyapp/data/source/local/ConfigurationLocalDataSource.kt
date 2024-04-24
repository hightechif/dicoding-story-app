package com.fadhil.storyapp.data.source.local

import com.fadhil.storyapp.data.source.local.prefs.ConfigurationLocalSource
import com.fadhil.storyapp.data.source.local.prefs.HttpHeaderLocalSource
import com.fadhil.storyapp.domain.model.Configuration
import javax.inject.Inject

class ConfigurationLocalDataSource @Inject constructor(
    private val configurationLocalSource: ConfigurationLocalSource,
    private val httpHeaderLocalSource: HttpHeaderLocalSource
) {
    fun getConfiguration() = configurationLocalSource.getCached()
    fun saveConfiguration(config: Configuration) = configurationLocalSource.save(config)
    fun isLogged() = httpHeaderLocalSource.isLogged()
    fun setBearerToken(token: String) = httpHeaderLocalSource.setBearerToken(token)
    fun logout() = httpHeaderLocalSource.logout()
}
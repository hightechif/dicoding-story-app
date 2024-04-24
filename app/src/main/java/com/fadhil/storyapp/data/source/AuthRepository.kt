package com.fadhil.storyapp.data.source

import com.fadhil.storyapp.data.NetworkBoundProcessResource
import com.fadhil.storyapp.data.Result
import com.fadhil.storyapp.data.source.local.ConfigurationLocalDataSource
import com.fadhil.storyapp.data.source.remote.AuthRemoteDataSource
import com.fadhil.storyapp.data.source.remote.response.ApiResponse
import com.fadhil.storyapp.data.source.remote.response.ResLogin
import com.fadhil.storyapp.domain.model.Configuration
import com.fadhil.storyapp.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val remoteSource: AuthRemoteDataSource,
    private val configurationLocalDataSource: ConfigurationLocalDataSource
) : IAuthRepository {

    override fun register(
        name: String,
        email: String,
        password: String
    ): Flow<Result<ApiResponse<Any?>?>> =
        object : NetworkBoundProcessResource<ApiResponse<Any?>?, ApiResponse<Any?>?>() {
            override suspend fun createCall(): Result<ApiResponse<Any?>?> =
                remoteSource.register(name, email, password)

            override suspend fun callBackResult(data: ApiResponse<Any?>?): ApiResponse<Any?>? {
                val config = Configuration(
                    isLogin = true,
                    email = email,
                    firstLogin = true
                )
                configurationLocalDataSource.saveConfiguration(config)
                return data
            }
        }.asFlow()

    override fun login(email: String, password: String): Flow<Result<ResLogin?>> =
        object : NetworkBoundProcessResource<ResLogin?, ApiResponse<ResLogin>?>() {
            override suspend fun createCall(): Result<ApiResponse<ResLogin>?> =
                remoteSource.login(email, password)

            override suspend fun callBackResult(data: ApiResponse<ResLogin>?): ResLogin? {
                var config = configurationLocalDataSource.getConfiguration()
                if (config == null) {
                    config = Configuration(
                        isLogin = true,
                        email = email,
                        firstLogin = true
                    )
                }
                config.isLogin = true
                config.email = email
                configurationLocalDataSource.saveConfiguration(config)

                val token = data?.loginResult?.token
                if (token != null) {
                    configurationLocalDataSource.setBearerToken(token)
                }

                return data?.loginResult
            }
        }.asFlow()

    override fun logout(): Flow<Boolean> = flow {
        try {
            var config = configurationLocalDataSource.getConfiguration()
            if (config == null) {
                config = Configuration(
                    isLogin = false,
                    email = null,
                    firstLogin = false
                )
            }
            config.isLogin = false
            config.email = null

            configurationLocalDataSource.saveConfiguration(config)
            configurationLocalDataSource.logout()
            emit(true)
        } catch (_: Exception) {
            emit(false)
        }
    }

}
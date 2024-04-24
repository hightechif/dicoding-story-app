package com.fadhil.storyapp.domain.usecase

import com.fadhil.storyapp.data.Result
import com.fadhil.storyapp.data.source.AuthRepository
import com.fadhil.storyapp.data.source.remote.response.ApiResponse
import com.fadhil.storyapp.data.source.remote.response.ResLogin
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val repository: AuthRepository
) : IAuthUseCase {

    override fun register(
        name: String,
        email: String,
        password: String
    ): Flow<Result<ApiResponse<Any?>?>> =
        repository.register(name, email, password)

    override fun login(email: String, password: String): Flow<Result<ResLogin?>> =
        repository.login(email, password)

    override fun logout(): Flow<Boolean> = repository.logout()

}
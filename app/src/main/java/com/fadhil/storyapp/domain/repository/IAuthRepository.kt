package com.fadhil.storyapp.domain.repository

import com.fadhil.storyapp.data.Result
import com.fadhil.storyapp.data.source.remote.response.ApiResponse
import com.fadhil.storyapp.data.source.remote.response.ResLogin
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {

    fun register(name: String, email: String, password: String): Flow<Result<ApiResponse<Any?>?>>

    fun login(email: String, password: String): Flow<Result<ResLogin?>>

    fun logout(): Flow<Boolean>

}
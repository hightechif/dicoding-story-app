package com.fadhil.storyapp.data.source.remote

import com.fadhil.storyapp.data.Result
import com.fadhil.storyapp.data.source.remote.network.StoryApiService
import com.fadhil.storyapp.data.source.remote.request.ReqLogin
import com.fadhil.storyapp.data.source.remote.request.ReqRegister
import com.fadhil.storyapp.data.source.remote.response.ApiResponse
import com.fadhil.storyapp.data.source.remote.response.ResLogin
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val service: StoryApiService
): BaseRemoteDataSource() {

    suspend fun register(name: String, email: String, password: String): Result<ApiResponse<Any?>?> {
        val request = ReqRegister(name, email, password)
        return getResult { service.register(request) }
    }

    suspend fun login(email: String, password: String): Result<ApiResponse<ResLogin>?> {
        val request = ReqLogin(email, password)
        return getResult { service.login(request) }
    }

}
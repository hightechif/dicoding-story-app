package com.fadhil.storyapp.data.source.remote.network

import com.fadhil.storyapp.data.source.local.prefs.HttpHeaderLocalSource
import com.fadhil.storyapp.util.StringUtil.Empty
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class AuthInterceptor(
    httpHeaderLocalSource: HttpHeaderLocalSource
) : Interceptor {

    private val headers = httpHeaderLocalSource.getCached()

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = headers?.get("Authorization") ?: String.Empty
        Timber.d("token=$accessToken")
        val requestBuilder = chain.request().newBuilder()
        val request = if (accessToken.isNotEmpty()) {
            requestBuilder
                .addHeader("Authorization", accessToken)
                .build()
        } else requestBuilder.build()
        return chain.proceed(request)
    }

}
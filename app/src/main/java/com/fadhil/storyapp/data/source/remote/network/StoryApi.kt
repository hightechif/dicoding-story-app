package com.fadhil.storyapp.data.source.remote.network

import com.fadhil.storyapp.data.source.local.prefs.HttpHeaderLocalSource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object StoryApi {

    private const val BASE_URL = "https://story-api.dicoding.dev/v1/"

    fun buildLoggerInterceptor(): HttpLoggingInterceptor {
        val loggerInterceptor = HttpLoggingInterceptor()
        loggerInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return loggerInterceptor
    }

    fun buildAuthInterceptor(httpHeaderLocalSource: HttpHeaderLocalSource): AuthInterceptor {
        return AuthInterceptor(httpHeaderLocalSource)
    }

    fun buildClient(loggerInterceptor: HttpLoggingInterceptor, authInterceptor: AuthInterceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggerInterceptor)
        .addInterceptor(authInterceptor)
        .connectTimeout(100, TimeUnit.SECONDS)
        .readTimeout(100, TimeUnit.SECONDS)
        .build()

    /**
     * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
     * object.
     */
    fun buildRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     * A public Api object that exposes the lazy-initialized Retrofit service
     */
    fun buildApiService(retrofit: Retrofit): StoryApiService {
        val service: StoryApiService by lazy { retrofit.create(StoryApiService::class.java) }
        return service
    }

}
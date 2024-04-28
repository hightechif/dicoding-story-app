package com.fadhil.storyapp.data.source.remote

import com.fadhil.storyapp.constant.ErrorMessage
import com.fadhil.storyapp.data.Result
import com.fadhil.storyapp.data.source.remote.response.ApiContentResponse
import com.fadhil.storyapp.data.source.remote.response.ApiResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.MalformedJsonException
import okio.BufferedSource
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException
import java.nio.charset.Charset

/**
 * Abstract Base Data source class with error handling
 */
abstract class BaseRemoteDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
        try {
            val response = call()
            val code = response.code()
            if (response.isSuccessful) {
                val body = response.body()
                return if (body != null) {
                    if (body is ApiResponse<*>) {
                        if (body.isSuccess()) {
                            Result.Success(body)
                        } else {
                            Result.Error(code.toString(), body.message)
                        }
                    } else if (body is ApiContentResponse<*>) {
                        if (body.isSuccess()) {
                            Result.Success(body)
                        } else {
                            Result.Error(code.toString(), body.message)
                        }
                    } else {
                        Result.Success(body)
                    }
                } else {
                    Result.Error("BODYNULL", ErrorMessage().connection())
                }
            } else {
                if (code == 401) {
                    (return if (response.errorBody() != null) {
                        val bufferedSource: BufferedSource = response.errorBody()!!.source()
                        bufferedSource.request(Long.MAX_VALUE) // Buffer the entire body.

                        val json =
                            bufferedSource.buffer.clone().readString(Charset.forName("UTF8"))

                        try {
                            val badResponse = Gson().fromJson<ApiResponse<Any>?>(
                                json,
                                object : TypeToken<ApiResponse<Any>?>() {}.type
                            )
                            if (badResponse.loginResult != null) {
                                Result.Unauthorized(badResponse.message)
                            } else {
                                Result.Unauthorized(badResponse.message)
                            }
                        } catch (e: Exception) {
                            Result.Unauthorized(json)
                        }
                    } else {
                        Result.Unauthorized(null)
                    })
                } else
                    if (code == 400 || code == 500) {
                        if (response.errorBody() != null) {
                            val bufferedSource: BufferedSource = response.errorBody()!!.source()
                            bufferedSource.request(Long.MAX_VALUE) // Buffer the entire body.

                            val json =
                                bufferedSource.buffer.clone().readString(Charset.forName("UTF8"))

                            val badResponse = Gson().fromJson<ApiResponse<Any>?>(
                                json,
                                object : TypeToken<ApiResponse<Any>?>() {}.type
                            )
                            return Result.Error(code.toString(), badResponse.message)
                        }
                    } else if (code == 503) {
                        return Result.Error("503", ErrorMessage().http503())
                    }
            }
            return Result.Error(code.toString(), response.message())
        } catch (e: Exception) {
            return if (e is ConnectException || e is UnknownHostException ||
                e is MalformedJsonException
            ) {
                Result.Error("ConnectionError", ErrorMessage().connection())
            } else {
                Result.Error("999", ErrorMessage().system(e.message))
            }
        }
    }

}
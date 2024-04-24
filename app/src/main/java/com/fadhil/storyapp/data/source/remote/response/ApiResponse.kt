package com.fadhil.storyapp.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("loginResult")
    val loginResult: T?,
    @field:SerializedName("story")
    val story: T?
) {
    fun isSuccess() = !error
}

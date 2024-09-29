package com.fadhil.storyapp.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ApiContentResponse<T>(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("listStory")
    val listStory: List<T>
) {
    fun isSuccess() = !error
}

package com.fadhil.storyapp.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class ReqLogin(
    @field:SerializedName("email")
    val email: String,
    @field:SerializedName("password")
    val password: String
)

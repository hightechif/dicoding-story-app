package com.fadhil.storyapp.data.source.remote.response

import com.fadhil.storyapp.util.DateTimeUtil
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class ResStory(
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("description")
    val description: String,
    @field:SerializedName("photoUrl")
    val photoUrl: String?,
    @field:SerializedName("createdAt")
    val createdAt: String,
    @field:SerializedName("lat")
    val lat: Double?,
    @field:SerializedName("lon")
    val lon: Double?
) {

    fun getCreatedDate(): LocalDateTime = DateTimeUtil.getUTCLocalDate(createdAt)
    fun getCreatedTime(): Long =
        getCreatedDate().toInstant(DateTimeUtil.zoneOffsetUTC).toEpochMilli()

}
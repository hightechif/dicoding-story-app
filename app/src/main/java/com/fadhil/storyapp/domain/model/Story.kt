package com.fadhil.storyapp.domain.model

import com.fadhil.storyapp.util.DateTimeUtil
import java.time.LocalDateTime

data class Story(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String?,
    val createdDate: LocalDateTime,
    val lat: Double?,
    val lon: Double?
) {

    fun getCreatedDateDisplay(): String = DateTimeUtil.getUTCLocalDate(
        createdDate,
        "dd MMM yyyy, HH:mm:ss"
    )

}

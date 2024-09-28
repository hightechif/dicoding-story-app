package com.fadhil.storyapp.util

import com.fadhil.storyapp.domain.model.Story
import java.time.LocalDateTime

object DataDummy {
    fun generateDummyValidStory(): List<Story> {
        val list = listOf(
            Story("1", "Fadhil", "Well", null, LocalDateTime.now(), null, null),
            Story("2", "Fadhil", "Well", null, LocalDateTime.now(), null, null),
            Story("3", "Fadhil", "Well", null, LocalDateTime.now(), null, null),
            Story("4", "Fadhil", "Well", null, LocalDateTime.now(), null, null),
            Story("5", "Fadhil", "Well", null, LocalDateTime.now(), null, null),
            Story("6", "Fadhil", "Well", null, LocalDateTime.now(), null, null),
            Story("7", "Fadhil", "Well", null, LocalDateTime.now(), null, null),
            Story("8", "Fadhil", "Well", null, LocalDateTime.now(), null, null),
            Story("9", "Fadhil", "Well", null, LocalDateTime.now(), null, null),
            Story("10", "Fadhil", "Well", null, LocalDateTime.now(), null, null),
        )
        return list
    }

    fun generateDummyEmptyStory(): List<Story> {
        return emptyList()
    }
}
package com.fadhil.storyapp.util

import androidx.paging.PagingData
import com.fadhil.storyapp.domain.model.Story

object DataDummy {
    fun generateDummyEmptyPagedStory(): PagingData<Story> {
        val pagedStory = PagingData.empty<Story>()
        return pagedStory
    }
}
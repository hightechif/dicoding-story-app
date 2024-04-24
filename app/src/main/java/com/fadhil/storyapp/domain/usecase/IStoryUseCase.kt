package com.fadhil.storyapp.domain.usecase

import com.fadhil.storyapp.data.Result
import com.fadhil.storyapp.domain.model.Story
import kotlinx.coroutines.flow.Flow

interface IStoryUseCase {

    fun getAllStory(
        page: Int?,
        size: Int?,
        location: Int?,
        reload: Boolean
    ): Flow<Result<List<Story>>>

    fun getStoryDetail(id: String, reload: Boolean): Flow<Result<Story?>>

}
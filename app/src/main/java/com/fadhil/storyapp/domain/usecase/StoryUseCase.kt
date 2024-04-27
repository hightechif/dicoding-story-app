package com.fadhil.storyapp.domain.usecase

import android.content.Context
import android.net.Uri
import com.fadhil.storyapp.data.Result
import com.fadhil.storyapp.data.source.StoryRepository
import com.fadhil.storyapp.domain.model.Story
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StoryUseCase @Inject constructor(
    private val repository: StoryRepository
) : IStoryUseCase {

    override fun getAllStory(
        page: Int?,
        size: Int?,
        location: Int?,
        reload: Boolean
    ): Flow<Result<List<Story>>> =
        repository.getAllStories(page, size, location, reload)

    override fun getStoryDetail(id: String, reload: Boolean): Flow<Result<Story?>> =
        repository.getStoryDetail(id, reload)

    override fun addNewStory(
        context: Context,
        description: String,
        uri: Uri,
        lat: Double?,
        lon: Double?
    ) = repository.addNewStory(context, description, uri, lat, lon)

}
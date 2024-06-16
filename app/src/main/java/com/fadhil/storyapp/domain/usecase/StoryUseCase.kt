package com.fadhil.storyapp.domain.usecase

import android.content.Context
import android.net.Uri
import com.fadhil.storyapp.data.source.StoryRepository
import javax.inject.Inject

class StoryUseCase @Inject constructor(
    private val repository: StoryRepository
) : IStoryUseCase {

    override fun getAllStory(
        page: Int?,
        size: Int?,
        location: Int?,
        reload: Boolean
    ) = repository.getAllStories(page, size, location, reload)

    override fun getPagingStory(
        page: Int?,
        size: Int?,
        location: Int?,
        reload: Boolean
    ) = repository.getPagingStory(page, size, location, reload)

    override fun getStoryDetail(id: String, reload: Boolean) = repository.getStoryDetail(id, reload)

    override fun addNewStory(
        context: Context,
        description: String,
        uri: Uri,
        lat: Double?,
        lon: Double?
    ) = repository.addNewStory(context, description, uri, lat, lon)

}
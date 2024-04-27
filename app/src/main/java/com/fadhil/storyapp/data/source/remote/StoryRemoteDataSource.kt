package com.fadhil.storyapp.data.source.remote

import com.fadhil.storyapp.data.Result
import com.fadhil.storyapp.data.source.remote.network.StoryApiService
import com.fadhil.storyapp.data.source.remote.response.FileUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class StoryRemoteDataSource @Inject constructor(
    private val service: StoryApiService
) : BaseRemoteDataSource() {

    suspend fun addNewStory(
        photo: MultipartBody.Part,
        request: RequestBody
    ): Result<FileUploadResponse?> {
        return getResult { service.addNewStory(photo, request) }
    }

    suspend fun addNewStoryAsGuest(
        file: MultipartBody.Part,
        description: RequestBody
    ): Result<FileUploadResponse?> {
        return getResult { service.addNewStoryAsGuest(file, description) }
    }

    suspend fun getAllStories(page: Int?, size: Int?, location: Int?) =
        getResult { service.getAllStories(page, size, location) }

    suspend fun getStoryDetail(id: String) = getResult { service.getStoryDetail(id) }

}
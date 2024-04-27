package com.fadhil.storyapp.domain.repository

import android.content.Context
import android.net.Uri
import com.fadhil.storyapp.data.Result
import com.fadhil.storyapp.data.source.remote.response.ApiResponse
import com.fadhil.storyapp.data.source.remote.response.FileUploadResponse
import com.fadhil.storyapp.domain.model.Story
import kotlinx.coroutines.flow.Flow

interface IStoryRepository {

    fun addNewStory(
        context: Context,
        description: String,
        uri: Uri,
        lat: Double?,
        lon: Double?
    ): Flow<Result<FileUploadResponse?>>

    fun addNewStoryAsGuest(
        context: Context,
        description: String,
        uri: Uri,
        lat: Double?,
        lon: Double?
    ): Flow<Result<FileUploadResponse?>>

    fun getAllStories(
        page: Int?,
        size: Int?,
        location: Int?,
        reload: Boolean
    ): Flow<Result<List<Story>>>

    fun getStoryDetail(id: String, reload: Boolean): Flow<Result<Story?>>

}
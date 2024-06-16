package com.fadhil.storyapp.domain.usecase

import android.content.Context
import android.net.Uri
import androidx.paging.PagingData
import com.fadhil.storyapp.data.Result
import com.fadhil.storyapp.data.source.remote.response.FileUploadResponse
import com.fadhil.storyapp.domain.model.Story
import kotlinx.coroutines.flow.Flow

interface IStoryUseCase {

    fun getAllStory(
        page: Int?,
        size: Int?,
        location: Int?,
        reload: Boolean
    ): Flow<Result<List<Story>>>

    fun getPagingStory(
        page: Int?,
        size: Int?,
        location: Int?,
        reload: Boolean
    ): Flow<PagingData<Story>>

    fun getStoryDetail(id: String, reload: Boolean): Flow<Result<Story?>>

    fun addNewStory(
        context: Context,
        description: String,
        uri: Uri,
        lat: Double?,
        lon: Double?
    ): Flow<Result<FileUploadResponse?>>

}
package com.fadhil.storyapp.data.source

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.fadhil.storyapp.data.NetworkBoundProcessResource
import com.fadhil.storyapp.data.NetworkBoundResource
import com.fadhil.storyapp.data.Result
import com.fadhil.storyapp.data.source.local.StoryLocalDataSource
import com.fadhil.storyapp.data.source.remote.StoryRemoteDataSource
import com.fadhil.storyapp.data.source.remote.response.ApiContentResponse
import com.fadhil.storyapp.data.source.remote.response.ApiResponse
import com.fadhil.storyapp.data.source.remote.response.FileUploadResponse
import com.fadhil.storyapp.data.source.remote.response.ResStory
import com.fadhil.storyapp.domain.mapper.StoryMapper
import com.fadhil.storyapp.domain.model.Story
import com.fadhil.storyapp.domain.repository.IStoryRepository
import com.fadhil.storyapp.util.FileUtil
import com.fadhil.storyapp.util.reduceFileImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.mapstruct.factory.Mappers
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val remoteDataSource: StoryRemoteDataSource,
    private val localDataSource: StoryLocalDataSource,
    private val storyRemoteMediator: StoryRemoteMediator,
    private val storyPagingSource: StoryPagingSource
) : IStoryRepository {

    private val mapper = Mappers.getMapper(StoryMapper::class.java)

    override fun addNewStory(
        context: Context,
        description: String,
        uri: Uri,
        lat: Double?,
        lon: Double?
    ): Flow<Result<FileUploadResponse?>> =
        object : NetworkBoundProcessResource<FileUploadResponse?, FileUploadResponse?>() {
            override suspend fun createCall(): Result<FileUploadResponse?> {
                val imageFile = uriToFile(uri, context).reduceFileImage()

                val multipartBody = createPart("photo", imageFile)
                val descReqBody = description.toRequestBody("text/plain".toMediaType())
                val latReqBody = lat?.toString()?.toRequestBody("text/plain".toMediaType())
                val lonReqBody = lon?.toString()?.toRequestBody("text/plain".toMediaType())

                return remoteDataSource.addNewStory(
                    multipartBody,
                    descReqBody,
                    latReqBody,
                    lonReqBody
                )
            }

            override suspend fun callBackResult(data: FileUploadResponse?): FileUploadResponse? {
                return data
            }
        }.asFlow()

    override fun addNewStoryAsGuest(
        context: Context,
        description: String,
        uri: Uri,
        lat: Double?,
        lon: Double?
    ): Flow<Result<FileUploadResponse?>> =
        object : NetworkBoundProcessResource<FileUploadResponse?, FileUploadResponse?>() {
            override suspend fun createCall(): Result<FileUploadResponse?> {
                val imageFile = uriToFile(uri, context).reduceFileImage()

                val multipartBody = createPart("photo-guest", imageFile)
                val requestBody = description.toRequestBody("text/plain".toMediaType())

                return remoteDataSource.addNewStoryAsGuest(multipartBody, requestBody)
            }

            override suspend fun callBackResult(data: FileUploadResponse?): FileUploadResponse? {
                return data
            }
        }.asFlow()

    private fun createPart(
        imageName: String,
        imageFile: File
    ): MultipartBody.Part {
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        return MultipartBody.Part.createFormData(
            imageName,
            imageFile.name,
            requestImageFile
        )
    }

    override fun getAllStories(
        page: Int?,
        size: Int?,
        location: Int?,
        reload: Boolean
    ): Flow<Result<List<Story>>> =
        object : NetworkBoundResource<List<Story>, ApiContentResponse<List<ResStory>>?>() {
            override fun loadFromDB(): Flow<List<Story>> {
                return localDataSource.getStories().map {
                    mapper.mapStoryEntityToDomainList(it)
                }
            }

            override suspend fun createCall(): Result<ApiContentResponse<List<ResStory>>?> {
                return remoteDataSource.getAllStories(page, size, location)
            }

            override suspend fun saveCallResult(data: ApiContentResponse<List<ResStory>>?) {
                coroutineScope {
                    launch(Dispatchers.IO) {
                        val stories =
                            mapper.mapStoryResponseToEntityList(data?.listStory ?: emptyList())
                        localDataSource.insertStory(stories)
                    }
                }
            }

            override fun shouldFetch(data: List<Story>?) = data?.isNotEmpty() != true || reload

        }.asFlow()

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagingStory(
        size: Int?,
        location: Int?,
        reload: Boolean
    ): LiveData<PagingData<Story>> {
        storyPagingSource.location = location ?: 1
        val pagingSourceFactory = { storyPagingSource }
        storyRemoteMediator.location = location ?: 1
        val pager = Pager(
            config = PagingConfig(
                pageSize = size ?: 1,
                enablePlaceholders = false
            ),
            remoteMediator = storyRemoteMediator,
            pagingSourceFactory = pagingSourceFactory
        )

        return pager.liveData
    }

    override fun getStoryDetail(id: String): Flow<Result<Story?>> =
        object : NetworkBoundProcessResource<Story?, ApiResponse<ResStory>?>() {

            override suspend fun createCall(): Result<ApiResponse<ResStory>?> {
                return remoteDataSource.getStoryDetail(id)
            }

            override suspend fun callBackResult(data: ApiResponse<ResStory>?): Story? {
                return data?.story?.let { mapper.mapStoryResponseToDomain(it) }
            }

        }.asFlow()

    private fun uriToFile(imageUri: Uri, context: Context): File {
        val myFile = FileUtil.createCustomTempFile(context)
        val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
        val outputStream = FileOutputStream(myFile)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0)
            outputStream.write(buffer, 0, length)
        outputStream.close()
        inputStream.close()
        return myFile
    }

    companion object {
        @Volatile
        private var instance: IStoryRepository? = null
        fun getInstance(
            remoteDataSource: StoryRemoteDataSource,
            localDataSource: StoryLocalDataSource,
            storyRemoteMediator: StoryRemoteMediator,
            storyPagingSource: StoryPagingSource
        ): IStoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(
                    remoteDataSource,
                    localDataSource,
                    storyRemoteMediator,
                    storyPagingSource
                )
            }.also { instance = it }
    }

}
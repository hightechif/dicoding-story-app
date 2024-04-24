package com.fadhil.storyapp.data.source

import android.content.Context
import android.net.Uri
import com.fadhil.storyapp.data.NetworkBoundProcessResource
import com.fadhil.storyapp.data.NetworkBoundResource
import com.fadhil.storyapp.data.Result
import com.fadhil.storyapp.data.source.local.StoryLocalDataSource
import com.fadhil.storyapp.data.source.remote.StoryRemoteDataSource
import com.fadhil.storyapp.data.source.remote.request.ReqStory
import com.fadhil.storyapp.data.source.remote.response.ApiContentResponse
import com.fadhil.storyapp.data.source.remote.response.ApiResponse
import com.fadhil.storyapp.data.source.remote.response.ResStory
import com.fadhil.storyapp.domain.mapper.StoryMapper
import com.fadhil.storyapp.domain.model.Story
import com.fadhil.storyapp.domain.repository.IStoryRepository
import com.fadhil.storyapp.util.FileUtil
import com.fadhil.storyapp.util.ImageUtils
import com.fadhil.storyapp.util.ImageUtils.isImage
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
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
    private val localDataSource: StoryLocalDataSource
) : IStoryRepository {

    private val mapper = Mappers.getMapper(StoryMapper::class.java)

    override fun addNewStory(
        context: Context,
        description: String,
        uri: Uri,
        lat: Double?,
        lon: Double?
    ): Flow<Result<ApiResponse<Any?>?>> =
        object : NetworkBoundProcessResource<ApiResponse<Any?>?, ApiResponse<Any?>?>() {
            override suspend fun createCall(): Result<ApiResponse<Any?>?> {
                val part = createPart(context, uri)
                val request = ReqStory(description, uriToFile(uri, context), lat, lon)
                return remoteDataSource.addNewStory(part, request)
            }

            override suspend fun callBackResult(data: ApiResponse<Any?>?): ApiResponse<Any?>? {
                return data
            }
        }.asFlow()

    override fun addNewStoryAsGuest(
        context: Context,
        description: String,
        uri: Uri,
        lat: Double?,
        lon: Double?
    ): Flow<Result<ApiResponse<Any?>?>> =
        object : NetworkBoundProcessResource<ApiResponse<Any?>?, ApiResponse<Any?>?>() {
            override suspend fun createCall(): Result<ApiResponse<Any?>?> {
                val part = createPart(context, uri)
                val request = ReqStory(description, uriToFile(uri, context), lat, lon)
                return remoteDataSource.addNewStoryAsGuest(part, request)
            }

            override suspend fun callBackResult(data: ApiResponse<Any?>?): ApiResponse<Any?>? {
                return data
            }
        }.asFlow()

    private suspend fun createPart(context: Context, uri: Uri): MultipartBody.Part {
        val inputStream = context.contentResolver.openInputStream(uri)
        val byteArray = inputStream!!.readBytes()

        val isImageFile = uri.isImage(context)

        val part = if (isImageFile) {
            val file = ImageUtils.createImageFile(context)
            val fos = FileOutputStream(file)
            fos.write(byteArray)
            inputStream.close()
            fos.close()

            try {
                Compressor.compress(context, file) {
                    resolution(480, 640)
                    quality(50)
                    size(5_242_880)
                    destination(file)
                }
            } catch (_: java.lang.Exception) {
            }

            val filePart = file.asRequestBody(
                context.contentResolver.getType(uri)!!.toMediaTypeOrNull()
            )

            MultipartBody.Part.createFormData("file", file.name, filePart)
        } else {
            val filePart = byteArray.toRequestBody(
                context.contentResolver.getType(uri)!!.toMediaTypeOrNull()
            )
            MultipartBody.Part.createFormData(
                "file",
                ImageUtils.getFileName(context, uri),
                filePart
            )
        }
        return part
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
                        data?.listStory?.forEach { story ->
                            val storyEntity = mapper.mapStoryResponseToEntity(story)
                            localDataSource.insertStory(storyEntity)
                        }
                    }
                }
            }

            override fun shouldFetch(data: List<Story>?) = data?.isNotEmpty() != true || reload

        }.asFlow()

    override fun getStoryDetail(id: String, reload: Boolean): Flow<Result<Story?>> =
        object : NetworkBoundResource<Story?, ApiResponse<ResStory>?>() {
            override fun loadFromDB(): Flow<Story?> {
                return localDataSource.getStories().map { list ->
                    val story = list.find { it.id == id }
                    story?.let { mapper.mapStoryEntityToDomain(it) }
                }
            }

            override suspend fun createCall(): Result<ApiResponse<ResStory>?> {
                return remoteDataSource.getStoryDetail(id)
            }

            override suspend fun saveCallResult(data: ApiResponse<ResStory>?) {
            }

            override fun shouldFetch(data: Story?) = data == null || reload

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

}
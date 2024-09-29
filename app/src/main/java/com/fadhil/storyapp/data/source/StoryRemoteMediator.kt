package com.fadhil.storyapp.data.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.fadhil.storyapp.constant.ErrorMessage
import com.fadhil.storyapp.data.Result
import com.fadhil.storyapp.data.source.local.db.AppDatabase
import com.fadhil.storyapp.data.source.local.entity.RemoteKeys
import com.fadhil.storyapp.data.source.local.entity.StoryEntity
import com.fadhil.storyapp.data.source.remote.network.StoryApiService
import com.fadhil.storyapp.data.source.remote.response.ApiContentResponse
import com.fadhil.storyapp.data.source.remote.response.ApiResponse
import com.fadhil.storyapp.domain.mapper.StoryMapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.MalformedJsonException
import okio.BufferedSource
import org.mapstruct.factory.Mappers
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException
import java.nio.charset.Charset
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator @Inject constructor(
    private val database: AppDatabase,
    private val apiService: StoryApiService
) : RemoteMediator<Int, StoryEntity>() {

    private val mapper = Mappers.getMapper(StoryMapper::class.java)
    var location: Int = 1

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, StoryEntity>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val responseData = apiService.getAllStories(page, state.config.pageSize, location)
            val resStory = getResult { responseData }
            val data = resStory.data?.listStory?.let { mapper.mapStoryResponseToEntityList(it) }
                ?: emptyList()
            val endOfPaginationReached = data.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().deleteRemoteKeys()
                    database.storyDao().deleteAll()
                }
                val prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = data.map {
                    RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao().insertAll(keys)
                database.storyDao().insertStory(data)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

    private suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
        try {
            val response = call()
            val code = response.code()
            if (response.isSuccessful) {
                val body = response.body()
                return if (body != null) {
                    if (body is ApiResponse<*>) {
                        if (body.isSuccess()) {
                            Result.Success(body)
                        } else {
                            Result.Error(code.toString(), body.message)
                        }
                    } else if (body is ApiContentResponse<*>) {
                        if (body.isSuccess()) {
                            Result.Success(body)
                        } else {
                            Result.Error(code.toString(), body.message)
                        }
                    } else {
                        Result.Success(body)
                    }
                } else {
                    Result.Error("BODYNULL", ErrorMessage().connection())
                }
            } else {
                if (code == 401) {
                    (return if (response.errorBody() != null) {
                        val bufferedSource: BufferedSource = response.errorBody()!!.source()
                        bufferedSource.request(Long.MAX_VALUE) // Buffer the entire body.

                        val json =
                            bufferedSource.buffer.clone().readString(Charset.forName("UTF8"))

                        try {
                            val badResponse = Gson().fromJson<ApiResponse<Any>?>(
                                json,
                                object : TypeToken<ApiResponse<Any>?>() {}.type
                            )
                            if (badResponse.loginResult != null) {
                                Result.Unauthorized(badResponse.message)
                            } else {
                                Result.Unauthorized(badResponse.message)
                            }
                        } catch (e: Exception) {
                            Result.Unauthorized(json)
                        }
                    } else {
                        Result.Unauthorized(null)
                    })
                } else
                    if (code == 400 || code == 500) {
                        if (response.errorBody() != null) {
                            val bufferedSource: BufferedSource = response.errorBody()!!.source()
                            bufferedSource.request(Long.MAX_VALUE) // Buffer the entire body.

                            val json =
                                bufferedSource.buffer.clone().readString(Charset.forName("UTF8"))

                            val badResponse = Gson().fromJson<ApiResponse<Any>?>(
                                json,
                                object : TypeToken<ApiResponse<Any>?>() {}.type
                            )
                            return Result.Error(code.toString(), badResponse.message)
                        }
                    } else if (code == 503) {
                        return Result.Error("503", ErrorMessage().http503())
                    }
            }
            return Result.Error(code.toString(), response.message())
        } catch (e: Exception) {
            return if (e is ConnectException || e is UnknownHostException ||
                e is MalformedJsonException
            ) {
                Result.Error("ConnectionError", ErrorMessage().connection())
            } else {
                Result.Error("999", ErrorMessage().system(e.message))
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 0
    }

}
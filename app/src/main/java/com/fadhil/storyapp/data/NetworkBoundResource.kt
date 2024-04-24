package com.fadhil.storyapp.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

abstract class NetworkBoundResource<ResultType, RequestType> {
    private val result: Flow<Result<ResultType>> = flow {
        emit(Result.Loading())
        val dbSource = loadFromDB().first()
        if (shouldFetch(dbSource)) {
            val response = createCall()
            when (response.status) {
                Result.Status.SUCCESS -> {
                    if (response.data != null) {
                        saveCallResult(response.data)
                        emitAll(
                            loadFromDB().map {
                                Result.Success(it)
                            }
                        )
                    } else {
                        Result.Error(
                            response.code,
                            response.message,
                            loadFromDB()
                        )
                    }
                }

                Result.Status.UNAUTHORIZED -> {
                    emitAll(loadFromDB().map { Result.Unauthorized(response.message, it) })
                }

                Result.Status.LOADING -> {}
                else -> {
                    emitAll(
                        loadFromDB().map {
                            Result.Error(
                                response.code,
                                response.message,
                                it
                            )
                        }
                    )
                }
            }
        } else {
            emitAll(
                loadFromDB().map {
                    Result.Success(it)
                }
            )
        }
    }

    protected abstract fun loadFromDB(): Flow<ResultType>

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract suspend fun createCall(): Result<RequestType>

    protected abstract suspend fun saveCallResult(data: RequestType)

    fun asFlow(): Flow<Result<ResultType>> = result

}
package com.fadhil.storyapp.data.source.remote.network

import com.fadhil.storyapp.data.source.remote.request.ReqLogin
import com.fadhil.storyapp.data.source.remote.request.ReqRegister
import com.fadhil.storyapp.data.source.remote.request.ReqStory
import com.fadhil.storyapp.data.source.remote.response.ApiContentResponse
import com.fadhil.storyapp.data.source.remote.response.ApiResponse
import com.fadhil.storyapp.data.source.remote.response.ResLogin
import com.fadhil.storyapp.data.source.remote.response.ResStory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface StoryApiService {

    @POST("register")
    suspend fun register(@Body body: ReqRegister): Response<ApiResponse<Any?>?>

    @POST("login")
    suspend fun login(@Body body: ReqLogin): Response<ApiResponse<ResLogin>?>

    @Multipart
    @POST("stories")
    suspend fun addNewStory(@Part("photo") photo: MultipartBody.Part, @Part("description") body: ReqStory): Response<ApiResponse<Any?>?>

    @Multipart
    @POST("stories/guest")
    suspend fun addNewStoryAsGuest(
        @Part("photo") photo: MultipartBody.Part,
        @Part("description") description: ReqStory
    ): Response<ApiResponse<Any?>?>

    @GET("stories")
    suspend fun getAllStories(
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Int?
    ): Response<ApiContentResponse<List<ResStory>>?>

    @GET("stories/{id}")
    suspend fun getStoryDetail(
        @Path("id") id: String,
    ): Response<ApiResponse<ResStory>?>

}
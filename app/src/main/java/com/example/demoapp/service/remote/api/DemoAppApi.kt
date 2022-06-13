package com.example.demoapp.service.remote.api

import com.example.demoapp.service.remote.request.LoginRequest
import com.example.demoapp.service.remote.request.RegisterRequest
import com.example.demoapp.service.remote.response.BaseResponse
import com.example.demoapp.service.remote.response.LoginResponse
import com.example.demoapp.service.remote.response.SharingResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface DemoAppApi {
    @POST(value = PATH_REGISTER)
    suspend fun register(
        @Body request: RegisterRequest,
    ): Response<BaseResponse>

    @POST(value = PATH_LOGIN)
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @GET(value = PATH_SHARING)
    suspend fun getSharing(
        @Header("Authorization") token: String
    ): Response<SharingResponse>

    @Multipart
    @POST(value = PATH_SHARING)
    suspend fun addSharing(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("alamat") alamat: RequestBody,
        @Part("Expire") expire: RequestBody,
        @Part("Contact") contact: RequestBody
    ): Response<BaseResponse>
}
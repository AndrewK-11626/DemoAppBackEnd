package com.example.demoapp.service.remote

import androidx.annotation.VisibleForTesting
import com.example.demoapp.model.InputAddSharing
import com.example.demoapp.service.remote.api.DemoAppApi
import com.example.demoapp.service.remote.request.LoginRequest
import com.example.demoapp.service.remote.request.RegisterRequest
import com.example.demoapp.service.remote.response.BaseResponse
import com.example.demoapp.service.remote.response.LoginResponse
import com.example.demoapp.service.remote.response.SharingResponse
import com.example.demoapp.utils.EMPTY_ERROR
import com.example.demoapp.utils.formatToken
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import timber.log.Timber

class RemoteDataSource(
    private val service: DemoAppApi
) {
    fun register(
        name: String,
        email: String,
        password: String,
    ): Flow<BaseResponse> = flow {
        val registerRequest = RegisterRequest(name, email, password)
        val response = service.register(registerRequest)

        Timber.d(registerRequest.toString())

        if (response.isSuccessful.not()) {
            emit(errorHandler(response))
            return@flow
        }

        val body = response.body() ?: throw IllegalStateException()
        Timber.d(body.message)
        emit(body)
    }
        .flowOn(Dispatchers.IO)
        .catch {
            emit(
                BaseResponse(
                    error = true,
                    message = EMPTY_ERROR
                )
            )
        }

    fun login(
        email: String,
        password: String
    ): Flow<LoginResponse> = flow {
        val loginRequest = LoginRequest(email, password)
        val response = service.login(loginRequest)

        Timber.d(loginRequest.toString())

        if (response.isSuccessful.not()) {
            val errorResponse = errorHandler(response)
            emit(LoginResponse().apply {
                error = errorResponse.error
                message = errorResponse.message
            })
            return@flow
        }

        val body = response.body() ?: throw IllegalStateException()
        Timber.d(body.message)
        emit(body)
    }
        .flowOn(Dispatchers.IO)
        .catch {
            emit(LoginResponse().apply {
                error = true
                message = EMPTY_ERROR
            })
        }
    fun getSharing(token: String): Flow<Result<SharingResponse>> = flow {
        val response = service.getSharing(token)

        if (response.isSuccessful.not()) {
            val errorBody = errorHandler(response)
            val exception = IllegalStateException(errorBody.message)
            emit(Result.failure(exception))
            return@flow
        }

        val body = response.body() ?: throw IllegalStateException()
        Timber.d(body.message)
        emit(Result.success(body))
    }
        .flowOn(Dispatchers.IO)
        .catch { emit(Result.failure(IllegalStateException(EMPTY_ERROR))) }

    fun addSharing(
        body: InputAddSharing
    ): Flow<BaseResponse> = flow {
        val token = body.token ?: throw IllegalStateException()
        val file = body.file ?: throw IllegalStateException()
        val desc = body.description ?: throw IllegalStateException()
        val alamat = body.description ?: throw IllegalStateException()
        val expire = body.description ?: throw IllegalStateException()
        val contact = body.description ?: throw IllegalStateException()
        val descRequest = desc.toRequestBody("text/plain".toMediaType())
        val alamatRequest = alamat.toRequestBody("text/plain".toMediaType())
        val expireRequest = expire.toRequestBody("text/plain".toMediaType())
        val contactRequest = contact.toRequestBody("text/plain".toMediaType())
        val imageRequest = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            imageRequest
        )

        val response = service.addSharing(token.formatToken(), imagePart, descRequest,alamatRequest,expireRequest,contactRequest)

        if (response.isSuccessful.not()) {
            emit(errorHandler(response))
            return@flow
        }

        val responseBody = response.body() ?: throw IllegalStateException()
        Timber.d(responseBody.message)
        emit(responseBody)
    }
        .flowOn(Dispatchers.IO)
        .catch {
            emit(BaseResponse(error = true, message = EMPTY_ERROR))
        }
    @VisibleForTesting
    fun <T> errorHandler(response: Response<T>): BaseResponse {
        val errorBody = response.errorBody()?.string()
        Timber.e(errorBody)
        return GsonBuilder().create().fromJson(errorBody, BaseResponse::class.java)
    }
}
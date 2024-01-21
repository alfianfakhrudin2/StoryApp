package com.example.storyapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.example.storyapp.data.lokal.database.StoryDatabase
import com.example.storyapp.data.lokal.mediator.StoryRemoteMediator
import com.example.storyapp.data.remote.response.ErrorResponse
import com.example.storyapp.data.remote.response.FileUploadResponse
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.data.remote.response.LoginResponse
import com.example.storyapp.data.remote.response.RegisterResponse
import com.example.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import com.example.storyapp.data.remote.response.Result
import com.example.storyapp.data.remote.response.StoryResponse
import com.example.storyapp.data.remote.retrofit.ApiConfig
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import com.example.storyapp.view.utils.wrapEspressoIdlingResource
import retrofit2.HttpException
import java.io.File

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val database: StoryDatabase
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun register(
        name: String,
        email: String,
        pass: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, pass)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            Log.d("register", e.message.toString())
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        }
    }

    fun login(
        email: String,
        pass: String
    ): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        wrapEspressoIdlingResource {
            try {
                val response = apiService.login(email, pass)
                emit(Result.Success(response))
            } catch (e: HttpException) {
                Log.d("login", e.message.toString())
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                emit(Result.Error(errorMessage.toString()))
            }
        }
    }

    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),

            remoteMediator = StoryRemoteMediator(database, apiService),
            pagingSourceFactory = {
                database.quoteDao().getAllQuote()
            }
        ).liveData
    }

    fun getStoriesWithLocation(location: Int = 1): LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = userPreference.getSession().first().token

            val api = ApiConfig.getApiService(token)

            val storiesLocResponse = api.getStoriesWithLocation(location)
            if (storiesLocResponse.error == false) {
                emit(Result.Success(storiesLocResponse))
            } else {
                emit(Result.Error(storiesLocResponse.message ?: "Location Not Found"))
            }
        } catch (e: HttpException) {
            Log.d("login", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }

    fun uploadImage(
        imageFile: File,
        description: String,
        lat: Double? = null,
        lon: Double? = null
    ) = liveData {
        emit(Result.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        //latlon
        val requestLat = lat?.toString()?.toRequestBody("text/plain".toMediaType())
        val requestLon = lon?.toString()?.toRequestBody("text/plain".toMediaType())

        try {
            val successResponse =
                apiService.uploadImage(multipartBody, requestBody, requestLat, requestLon)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()

            val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
            emit(Result.Error(errorResponse.message))
        }
    }

    companion object {
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
            database: StoryDatabase
        ): UserRepository =
            UserRepository(apiService, userPreference, database)
    }
}
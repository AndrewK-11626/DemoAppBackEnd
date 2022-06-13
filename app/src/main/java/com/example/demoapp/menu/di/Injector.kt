package com.example.demoapp.menu.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.demoapp.sharing.AddSharingValidation
import com.example.demoapp.autentikasi.login.LoginValidation
import com.example.demoapp.autentikasi.login.LoginViewModel
import com.example.demoapp.autentikasi.register.RegisterValidation
import com.example.demoapp.autentikasi.register.RegisterViewModel
import com.example.demoapp.model.dataStore
import com.example.demoapp.service.remote.RemoteDataSource
import com.example.demoapp.service.remote.api.BASE_URL
import com.example.demoapp.ui.sharing.SharingViewModel
import com.example.demoapp.autentikasi.splashscreen.SplashViewModel
import com.example.demoapp.service.remote.api.DemoAppApi
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("UNCHECKED_CAST")
class Injector(private val context: Context) {

    val userPreference by lazy { UserPreferenceInstance.get(context.dataStore) }

    private val remoteService: DemoAppApi by lazy {
        RetrofitInstance.get(
            baseUrl = BASE_URL,
            client = OkHttpClientInstance.get(),
            converter = GsonConverterFactory.create()
        ).create(DemoAppApi::class.java)
    }
    val remoteDataSource: RemoteDataSource by lazy {
        RemoteDataSource(remoteService)
    }
    val addSharingFactory by lazy {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SharingViewModel(
                    userPreference,
                    remoteDataSource,
                    AddSharingValidation()
                ) as T
            }
        }
    }
    val registerFactory by lazy {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return RegisterViewModel(remoteDataSource, RegisterValidation()) as T
            }
        }
    }

    val loginFactory by lazy {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(remoteDataSource, userPreference, LoginValidation()) as T
            }
        }
    }

    val splashFactory by lazy {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SplashViewModel(userPreference) as T
            }
        }
    }
}
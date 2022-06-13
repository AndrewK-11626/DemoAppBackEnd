package com.example.demoapp.autentikasi.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.demoapp.model.UserPreference
import kotlinx.coroutines.Dispatchers

class SplashViewModel(preference: UserPreference) : ViewModel() {
    val isLoggedIn = preference.isLoggedIn().asLiveData(Dispatchers.Main)
}
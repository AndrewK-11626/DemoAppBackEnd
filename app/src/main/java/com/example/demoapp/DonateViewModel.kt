package com.example.demoapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DonateViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "080123456789"
    }
    val text: LiveData<String> = _text

}
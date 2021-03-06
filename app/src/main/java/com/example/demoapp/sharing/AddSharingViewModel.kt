package com.example.demoapp.sharing

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demoapp.menu.validation.InputValidation
import com.example.demoapp.model.InputAddSharing
import com.example.demoapp.model.User
import com.example.demoapp.model.UserPreference
import com.example.demoapp.service.remote.RemoteDataSource
import com.example.demoapp.utils.*
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

class AddSharingViewModel (
    private val userPreference: UserPreference,
    private val remoteDataSource: RemoteDataSource,
    private val validation: InputValidation
) : ViewModel() {
    private val _errorMessage = MutableLiveEvent<Int>()
    val errorMessage: LiveEvent<Int> get() = _errorMessage

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = _loading

    private val _dialogInfoSuccess = MutableLiveData<String?>()
    val dialogInfoSuccess: LiveData<String?> get() = _dialogInfoSuccess

    private val _dialogInfoError = MutableLiveData<String?>()
    val dialogInfoError: LiveData<String?> get() = _dialogInfoError

    private val _userData = MutableLiveData<User>()

    private var imageFile: File? = null

    init {
        initialize()
    }

    @VisibleForTesting
    fun initialize() = viewModelScope.launch {
        userPreference.getUser().collect(_userData::setValue)
    }

    fun addStory(description: String?) = viewModelScope.launch {
        val token = _userData.value?.token

        val inputAddSharing = InputAddSharing(token, imageFile, description)
        val isInputValid = checkInput(inputAddSharing)
        if (!isInputValid) return@launch

        Timber.d(inputAddSharing.toString())

        remoteDataSource.addSharing(inputAddSharing)
            .onStart { _loading.postValue(true) }
            .onCompletion { _loading.postValue(false) }
            .collect {
                Timber.d(it.toString())
                val isError = it.error ?: true
                if (isError) {
                    val message = it.message ?: EMPTY_ERROR
                    _dialogInfoError.postValue(message)
                    Timber.e(message)
                } else {
                    val message = it.message ?: EMPTY_SUCCESS
                    _dialogInfoSuccess.postValue(message)
                }
            }
    }

    @VisibleForTesting
    fun checkInput(inputAddSharing: InputAddSharing): Boolean {
        val isInputValid = validation.validate(inputAddSharing) {
            _errorMessage.value = Event(it)
        }

        return isInputValid
    }

    fun dismissSuccessDialog() {
        _dialogInfoSuccess.value = null
    }

    fun dismissErrorDialog() {
        _dialogInfoError.value = null
    }

    fun setImageFile(file: File?) {
        imageFile = file
    }
}
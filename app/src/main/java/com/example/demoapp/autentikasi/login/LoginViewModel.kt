package com.example.demoapp.autentikasi.login

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demoapp.R
import com.example.demoapp.menu.validation.InputValidation
import com.example.demoapp.model.InputLogin
import com.example.demoapp.model.User
import com.example.demoapp.model.UserPreference
import com.example.demoapp.service.remote.RemoteDataSource
import com.example.demoapp.utils.EMPTY_ERROR
import com.example.demoapp.utils.Event
import com.example.demoapp.utils.LiveEvent
import com.example.demoapp.utils.MutableLiveEvent
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginViewModel (
    private val remoteDataSource: RemoteDataSource,
    private val userPreference: UserPreference,
    private val validation: InputValidation
) : ViewModel() {
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = _loading

    private val _errorMessage = MutableLiveEvent<Int>()
    val errorMessage: LiveEvent<Int> get() = _errorMessage

    private val _dialogInfoError = MutableLiveData<String?>()
    val dialogInfoError: LiveData<String?> get() = _dialogInfoError

    private val _loginSuccess = MutableLiveEvent<Unit>()
    val loginSuccess: LiveEvent<Unit> get() = _loginSuccess

    fun login(email: String?, password: String?) = viewModelScope.launch {
        val isInputValid = checkInput(email, password)
        if (!isInputValid) return@launch

        remoteDataSource.login(email!!, password!!)
            .onStart { _loading.postValue(true) }
            .onCompletion { _loading.postValue(false) }
            .collect {
                Timber.d(it.toString())
                val isError = it.error ?: true
                val message = it.message ?: EMPTY_ERROR
                if (isError) {
                    _dialogInfoError.postValue(message)
                    Timber.e(message)
                } else {
                    val id = it.loginResult?.userId.orEmpty()
                    val name = it.loginResult?.name.orEmpty()
                    val token = it.loginResult?.token.orEmpty()

                    if (token.isEmpty()) {
                        _errorMessage.value = Event(R.string.error_occur)
                    } else {
                        val user = User(id, name, token)
                        userPreference.saveUser(user)
                        _loginSuccess.postValue(Event(Unit))
                    }
                }
            }
    }

    fun dismissError() {
        _dialogInfoError.value = null
    }

    @VisibleForTesting
    fun checkInput(email: String?, password: String?): Boolean {
        val inputLogin = InputLogin(email, password)
        val isInputValid = validation.validate(inputLogin) {
            _errorMessage.value = Event(it)
        }

        return isInputValid
    }
}
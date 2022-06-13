package com.example.demoapp.ui.sharing

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.example.demoapp.menu.validation.InputValidation
import com.example.demoapp.model.Sharing
import com.example.demoapp.model.User
import com.example.demoapp.model.UserPreference
import com.example.demoapp.service.remote.RemoteDataSource
import com.example.demoapp.utils.EMPTY_TOKEN_ERROR
import com.example.demoapp.utils.formatToken
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

class SharingViewModel(
    private val userPreferences: UserPreference,
    private val remoteDataSource: RemoteDataSource,
    private val validation: InputValidation
) : ViewModel() {
    private val _userData = MutableLiveData<User>()

    private val _sharingData = MutableLiveData<List<Sharing>>()
    val storiesData: LiveData<List<Sharing>> get() = _sharingData

    private val _profileDialog = MutableLiveData<User?>()
    val profileDialog: LiveData<User?> get() = _profileDialog

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    val enableMenu
        get() = Transformations.map(_userData) {
            it.token.isNotEmpty()
        }

    init {
        initialize()
        getSharing()
    }

    @VisibleForTesting
    fun initialize() = viewModelScope.launch {
        userPreferences.getUser().collect(_userData::setValue)
    }

    fun getSharing() = viewModelScope.launch {
        val token = _userData.value?.token

        if (token == null) {
            _errorMessage.value = EMPTY_TOKEN_ERROR
            return@launch
        }

        remoteDataSource.getSharing(token.formatToken())
            .onStart { _loading.postValue(true) }
            .onCompletion { _loading.postValue(false) }
            .collect { result ->
                Timber.d(result.toString())
                result.fold(
                    onFailure = {
                        _errorMessage.value = it.message
                    },
                    onSuccess = {
                        val sharingRes = it.listSharing ?: emptyList()
                        val sharing = sharingRes.map { s ->
                            Sharing(
                                s.id.orEmpty(),
                                s.name.orEmpty(),
                                s.description.orEmpty(),
                                s.alamat.orEmpty(),
                                s.expire.orEmpty(),
                                s.contact.orEmpty(),
                                s.photoUrl.orEmpty(),
                                s.createdAt.orEmpty(),
                                s.latitude.orEmpty(),
                                s.longitude.orEmpty()
                            )
                        }
                        _sharingData.postValue(sharing)
                    }
                )
            }
    }

    fun showProfileDialog() {
        val user = _userData.value ?: return
        _profileDialog.value = user
    }

    fun dismissProfileDialog(){
        _profileDialog.value = null
    }

    fun logout() = viewModelScope.launch {
        userPreferences.logout()
    }
}
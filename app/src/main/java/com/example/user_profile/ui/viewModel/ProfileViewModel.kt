package com.example.user_profile.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.user_profile.model.ProfileResponse
import com.example.user_profile.network.ApiRepository
import com.example.user_profile.network.Resource
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ApiRepository) : ViewModel() {

    private val _user =
        MutableLiveData<Resource<ProfileResponse>>()
    val user: LiveData<Resource<ProfileResponse>> get() = _user

    fun fetchUser() {
        viewModelScope.launch {
            try {
                val result = repository.getAboutUsApi()
                _user.postValue((Resource.Success(result)))
            } catch (e: Exception) {
                e.printStackTrace()
                _user.postValue(Resource.Error(e.message ?: "Something went wrong"))
            }
        }
    }
}
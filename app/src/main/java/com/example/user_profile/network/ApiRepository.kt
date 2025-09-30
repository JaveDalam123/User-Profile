package com.example.user_profile.network

class ApiRepository {
    suspend fun getAboutUsApi() = RetrofitInstance.api.getProfileData()
}
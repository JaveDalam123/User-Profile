package com.example.user_profile.network

import com.example.user_profile.model.ProfileResponse
import retrofit2.http.GET
interface ApiService {
    @GET("android-assesment/profile/refs/heads/main/data.json")
    suspend fun getProfileData(): ProfileResponse
}
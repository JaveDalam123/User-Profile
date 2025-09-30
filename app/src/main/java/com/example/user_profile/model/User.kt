package com.example.user_profile.model

data class User(
    val avatar: String,
    val location: Location,
    val name: String,
    val social: Social,
    val statistics: Statistics,
    val username: String
)
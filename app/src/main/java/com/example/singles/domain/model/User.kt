package com.example.singles.domain.model

data class UserProfile(
    val age: String,
    val displayName: String,
    val fullName: String,
    val email:String?,
    val gender: String,
    val image0: String,
    val image1: String,
    val image2: String,
    val image3: String,
    val institution: String,
    val isAgreed: Boolean
)


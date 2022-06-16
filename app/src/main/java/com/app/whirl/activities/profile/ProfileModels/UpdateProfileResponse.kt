package com.app.whirl.activities.profile.ProfileModels

data class UpdateProfileResponse(
    val `data`: List<Data>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: Boolean
)
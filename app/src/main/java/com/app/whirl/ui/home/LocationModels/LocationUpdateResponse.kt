package com.app.whirl.ui.home.LocationModels

data class LocationUpdateResponse(
    val `data`: Boolean,
    val error: Int,
    val msg: String,
    val service: String,
    val success: Boolean
)
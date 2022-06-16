package com.app.whirl.ui.home.DriverTripModels

data class DriverTripResponse(
    val `data`: List<Data>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: Boolean
)
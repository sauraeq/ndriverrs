package com.app.whirl.ui.home.AcceptTripModels

data class AcceptTripResponse(

    val error: Int,
    val msg: String,
    val service: String,
    val success: Boolean
)
package com.app.whirl.ui.home.UpdateTokenModels

data class UpdateFCMTokenResponse(
    val `data`: List<Any>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)
package com.app.whirl.ui.home.OffLineDetailModels

data class OffLineDetailResponse(
    val `data`: ArrayList<Data>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: Boolean
)
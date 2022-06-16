package com.app.whirl.activities.earned.Models

data class EarnResponse(
    val `data`: ArrayList<Data>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: Boolean
)
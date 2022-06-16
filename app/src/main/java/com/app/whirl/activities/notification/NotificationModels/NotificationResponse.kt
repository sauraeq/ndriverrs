package com.app.whirl.activities.notification.NotificationModels

data class NotificationResponse(
    val `data`: ArrayList<Data>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: Boolean
)
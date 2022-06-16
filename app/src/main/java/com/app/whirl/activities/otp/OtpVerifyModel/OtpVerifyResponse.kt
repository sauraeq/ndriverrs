package com.app.whirl.activities.otp.OtpVerifyModel

data class OtpVerifyResponse(
    val `data`: Data,
    val error: Int,
    val msg: String,
    val service: String,
    val success: Boolean
)
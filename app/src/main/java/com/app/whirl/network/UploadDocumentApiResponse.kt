package com.app.whirl.network

import com.app.whirl.model.SignUpData
import com.app.whirl.model.UploadDocumentDTO
import com.google.gson.annotations.SerializedName

class UploadDocumentApiResponse {
    @SerializedName("success")
    var success:Boolean = false

    @SerializedName("error")
    var error:Int=0
    @SerializedName("msg")
    lateinit var msg:String
    @SerializedName("service")
    lateinit var service:String
   // @SerializedName("data")
    //lateinit var data: SignUpData
}
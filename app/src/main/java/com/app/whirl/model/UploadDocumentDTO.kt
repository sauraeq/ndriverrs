package com.app.whirl.model

import com.google.gson.annotations.SerializedName

class UploadDocumentDTO {
    @SerializedName("id"           ) var id          : String? = null
    @SerializedName("phone_no"     ) var phoneNo     : String? = null
    @SerializedName("otp"          ) var otp         : String? = null
    @SerializedName("status"       ) var status      : String? = null
    @SerializedName("created_date" ) var createdDate : String? = null
}
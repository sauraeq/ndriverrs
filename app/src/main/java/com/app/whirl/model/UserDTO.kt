package com.app.whirl.model

import com.google.gson.annotations.SerializedName

class UserDTO {
    @SerializedName("user_id"           ) var id          : String? =null
    @SerializedName("mobile"     ) var phoneNo     : String? = null
    @SerializedName("otp"          ) var otp         : String? = null
    @SerializedName("status"       ) var status      : String? = null
    @SerializedName("created_date" ) var createdDate : String? = null
    @SerializedName("step"           ) var step          : Int? =0

}
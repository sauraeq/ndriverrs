package com.app.whirl.network


import com.app.whirl.activities.earned.Models.EarnResponse
import com.app.whirl.activities.notification.NotificationModels.NotificationResponse
import com.app.whirl.activities.otp.OtpVerifyModel.OtpVerifyResponse
import com.app.whirl.activities.profile.ProfileModels.UpdateProfileResponse
import com.app.whirl.model.Login
import com.app.whirl.ui.home.AcceptTripModels.AcceptTripResponse
import com.app.whirl.ui.home.DriverTripModels.DriverTripResponse
import com.app.whirl.ui.home.LocationModels.LocationUpdateResponse
import com.app.whirl.ui.home.OffLineDetailModels.OffLineDetailResponse
import com.app.whirl.ui.home.UpdateTokenModels.UpdateFCMTokenResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface WhirlService {
    @GET("")
    fun otpCheck() : Call<List<Login>>

    @POST("driver/driverLoginotp")
 //   @FormUrlEncoded
//    fun loginOtp( @Field("mobile") mobile:String ):Call<List<Login>>
    fun loginOtp( @Body mobile:HashMap<String,String> ):Call<LoginApiResponse>

    @POST("driver/driverSignup")
 //   @FormUrlEncoded
    fun driverSignup( @Body mobile:HashMap<String,String>  ):Call<SignupApiResponse>


    @POST("driver/driverotpverify")
   // @FormUrlEncoded
    fun otpVerify( @Body mobile:HashMap<String,String>  ):Call<OtpVerifyResponse>


    @POST("driver/resend_otp")
    //   @FormUrlEncoded
    fun driverResendOtp( @Body mobile:HashMap<String,String>  ):Call<SignupApiResponse>
/* @Multipart
    @POST(ApiConstant.API_UPLOAD_IMAGE)
    Call<ApiResponse> uploadImage(@Header("Content-Language") String language, @Part MultipartBody.Part image,
                                  @Part("image_type") RequestBody imageType, @Part("result_key") RequestBody requestBody);
*/


@Multipart
    @POST("driver/editdriverProfile")
fun uploadDocument(@Part("userId")userId: RequestBody,
                   @Part("fname")fname: RequestBody,
                   @Part("lname")lname: RequestBody,
                   @Part("email")email: RequestBody,
                   @Part("earn_type")earnTYpe:RequestBody,
                   @Part("type")type:RequestBody,
                   @Part file: MultipartBody.Part?
                   ):Call<UploadDocumentApiResponse>

    @POST("driver/offlinehistory")
    fun getOffLineDetail( @Body request:HashMap<String,String>  ):Call<OffLineDetailResponse>

    @POST("driver/drivertrip")
    fun getDriverTrip( @Body request:HashMap<String,String>  ):Call<DriverTripResponse>

    @POST("driver/accepttrip")
    fun acceptTrip( @Body request:HashMap<String,String>  ):Call<AcceptTripResponse>

    @POST("driver/rejecttrip")
    fun rejectTrip( @Body request:HashMap<String,String>  ):Call<AcceptTripResponse>


    @POST("driver/driverlocation")
    fun updateLocation( @Body request:HashMap<String,String>  ):Call<LocationUpdateResponse>

    @POST("driver/completetrip")
    fun completeTrip( @Body request:HashMap<String,String>  ):Call<AcceptTripResponse>


    @Multipart
    @POST("driver/editdriverProfilenew")
    fun uploadDocument2(@Part("driver_id")userId: RequestBody?,
                       @Part("fname")fname: RequestBody?,
                       @Part("lname")lname: RequestBody?,
                       @Part("email")email: RequestBody?,
                       @Part("earn_type")earnTYpe:RequestBody?,
                       @Part profile: MultipartBody.Part?,@Part licence: MultipartBody.Part?,@Part pan: MultipartBody.Part?
                        ,@Part vehicleins: MultipartBody.Part?,@Part permit: MultipartBody.Part?,@Part rc: MultipartBody.Part?
    ):Call<UpdateProfileResponse>

    @Multipart
    @POST("driver/editdriverProfilenew")
    fun editDocument(@Part("driver_id")userId: RequestBody?,
                        @Part("fname")fname: RequestBody?,
                        @Part("lname")lname: RequestBody?,
                        @Part("email")email: RequestBody?,
                        @Part profile: MultipartBody.Part?
    ):Call<UpdateProfileResponse>

    @POST("driver/notification")
    fun getNotification( @Body request:HashMap<String,String>  ):Call<NotificationResponse>

    @POST("driver/allhistory")
    fun getEarnList( @Body request:HashMap<String,String>  ):Call<EarnResponse>


    @POST("driver/updatefcmtoken")
    fun updateFcmToken( @Body request:HashMap<String,String>  ):Call<UpdateFCMTokenResponse>

}
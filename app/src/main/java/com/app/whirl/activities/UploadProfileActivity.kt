package com.app.whirl.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.app.whirl.R
import com.app.whirl.network.ApiInterface
import com.app.whirl.network.SignupApiResponse
import com.app.whirl.network.UploadDocumentApiResponse
import com.github.dhaval2404.imagepicker.ImagePicker
import com.metaled.Utils.ConstantUtils
import com.metaled.Utils.SharedPreferenceUtils
import kotlinx.android.synthetic.main.activity_upload_profile.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.http.Part
import java.io.File


import retrofit2.converter.gson.GsonConverterFactory.create
import okhttp3.RequestBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.MultipartBody


class UploadProfileActivity : AppCompatActivity() {
    private lateinit var isProfileUri: Uri
    var isProfile: Boolean = false
    var email: String = ""
    var isLogin: Boolean = false
    var number: String = ""
    var fName: String = ""
    var lName: String = ""
    var earntype = ""
    var isProfileDoc = ""
    var isDrivingLicenceFrontUri = ""
    var isPanCardUri = ""
    var isRegistrationCertficateUri = ""
    var isVehicleInsuranceUri = ""
    var isVehiclePermitUri = ""

    var userId:String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_profile)
        getData()
        userId= SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.USER_ID,"")


        crdUploadPic.setOnClickListener {
            isProfile = true
            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

    }

    private fun getData() {
        number = intent.getStringExtra("number").toString()
        isLogin = intent.getBooleanExtra("isLogin", false)
        fName = intent.getStringExtra("fname").toString()
        lName = intent.getStringExtra("lname").toString()
        email = intent.getStringExtra("email").toString()
        earntype = intent.getStringExtra("vehicleType").toString()
        isProfileDoc = intent.getStringExtra("isProfileDoc").toString()
        isDrivingLicenceFrontUri = intent.getStringExtra("isDrivingLicenceFrontUri").toString()
        isPanCardUri = intent.getStringExtra("isPanCardUri").toString()
        isRegistrationCertficateUri =
            intent.getStringExtra("isRegistrationCertficateUri").toString()
        isVehicleInsuranceUri = intent.getStringExtra("isVehicleInsuranceUri").toString()
        isVehiclePermitUri = intent.getStringExtra("isVehiclePermitUri").toString()
    }

    fun onClickUpload(view: View) {

        when (view.id) {
            R.id.llProfilePhoto -> {
                isProfile = true
            }
        }


        ImagePicker.with(this)
            .crop()                    //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!

            // Use Uri object instead of File to avoid storage permissions
            //    imgProfile.setImageURI(fileUri)
            Log.d("uri", "onActivityResult: " + uri)
            setImageUri(uri)
            createMultiPart(""+userId,uri,11,"prfilephoto");
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createMultiPart(userId:String,doc1: Uri, requestCode: Int,type:String) {

        val progressDialog = ProgressDialog(this)
        //  progressDialog.setTitle("Kotlin Progress Bar")
        progressDialog.setMessage("Loading...")
        progressDialog.show()

//        Log.d("uri", "onActivityResult: ===>" + doc1.toString())
        var requestBody: RequestBody? = null
        val file = File(doc1.toString())

        val userId: RequestBody =userId.toRequestBody("text/plain".toMediaType())

        val fName: RequestBody =fName.toRequestBody("text/plain".toMediaType())

        val lName: RequestBody =lName.toRequestBody("text/plain".toMediaType())
        val email: RequestBody =email.toRequestBody("text/plain".toMediaType())

        val earnType: RequestBody ="2".toRequestBody("text/plain".toMediaType())
        var type: RequestBody ="prfilephoto".toRequestBody("text/plain".toMediaType())
        requestBody = file.path.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        //  Log.d("uri", "onActivityResult: ========>" + doc1.toString()+" file "+file.name+" path"+file.path)

        var imagenPerfil: MultipartBody.Part
        imagenPerfil= MultipartBody.Part.createFormData("imagenPerfil", file.name, requestBody);

        //Log.d("uri", "onActivityResult: ========>after part" + doc1.toString())

        val apiCall = ApiInterface.create().uploadDocument(userId,fName,lName,email,earnType,type,imagenPerfil)

        apiCall.enqueue(object : Callback<UploadDocumentApiResponse> {
            override fun onResponse(call: Call<UploadDocumentApiResponse>, response: Response<UploadDocumentApiResponse>) {
                //TODO("Not yet implemented")
                progressDialog.dismiss()
                if(response.isSuccessful){
                    if(response.body()!!.success){
                     var intent:  Intent=Intent()
                        setResult(RESULT_OK)
                        finish()
                      //  Toast.makeText(applicationContext,response.body()!!.msg,Toast.LENGTH_SHORT).show()

                    }else{
                        Toast.makeText(applicationContext,response.body()!!.msg,Toast.LENGTH_SHORT).show()
                    }

                }


            }

            override fun onFailure(call: Call<UploadDocumentApiResponse>, t: Throwable) {
                //  TODO("Not yet implemented")
            }


        })





    }



    private fun setImageUri(uri: Uri) {

        imvProfile.setImageURI(uri)
        isProfileUri = uri;
        var filePath = File(uri.path)
        //  Log.d("setImageUri", "setImageUri: " + filePath)
    }

    fun click(view: View) {
        if (isProfileUri != null) {

        }


//        ImagePicker.with(this)
//            .crop()                    //Crop image(Optional), Check Customization for more option
//            .compress(1024)            //Final image size will be less than 1 MB(Optional)
//            .maxResultSize(
//                1080,
//                1080
//            )    //Final image resolution will be less than 1080 x 1080(Optional)
//            .start()
    }

    fun onClickBack(view: View) {
        onBackPressed()

    }



}
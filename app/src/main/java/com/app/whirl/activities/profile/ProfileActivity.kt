package com.app.whirl.activities.profile

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.app.whirl.HomeActivity
import com.app.whirl.R
import com.app.whirl.activities.profile.ProfileModels.UpdateProfileResponse
import com.app.whirl.network.ApiInterface
import com.app.whirl.utils.Camerautils.FileCompressor
import com.app.whirl.utils.ToastUtil
import com.bumptech.glide.Glide
import com.example.ranierilavastone.Utils.StringUtil
import com.github.dhaval2404.imagepicker.ImagePicker
import com.metaled.Utils.ConstantUtils
import com.metaled.Utils.SharedPreferenceUtils

import kotlinx.android.synthetic.main.activity_profile.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class ProfileActivity : AppCompatActivity() {
    private var profileImage: File? = null

    private val CAMERA = 100
    private val CAMERA_PERMISSION_CODE = 101
    var image_uri: Uri? = null
    var email: String = ""
    var fName: String = ""
    var lName: String = ""
    var mCompressor: FileCompressor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        mCompressor = FileCompressor(this)
        Log.d("profileImage",SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.PROFILE_IMAGE,"")+"...")
        Glide.with(this).load(SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.PROFILE_IMAGE,"")).placeholder(resources.getDrawable(R.drawable.profile_icon)).into(ivProfile)

        etFName.setText(SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.FIRSTNAME,""))
        etLName.setText(SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.LASTNAME,""))
        etEmail.setText(SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.EMAIL_ID,""))
        tvMobileNo.setText(SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.USER_MOBILE_No,""))

        onclick()

    }

    fun onclick() {
        ivProfile.setOnClickListener {
            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        btnNext.setOnClickListener {
            fName=etFName.text.toString()
            lName=etLName.text.toString()
            email=etEmail.text.toString()
            if(fName.isNullOrEmpty()){
                ToastUtil.toast_Long(this,resources.getString(R.string.plzfname))
            }else if(lName.isNullOrEmpty()){
                ToastUtil.toast_Long(this,resources.getString(R.string.plzlname))
            }else if(email.isNullOrEmpty()){
                ToastUtil.toast_Long(this,resources.getString(R.string.plzemail))
            }else if(!StringUtil.isEmailValid(email)){
                ToastUtil.toast_Long(this,resources.getString(R.string.plzvalidemail))
            }else {
                update()
            }

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
                val uri: Uri = data?.data!!

                // Use Uri object instead of File to avoid storage permissions
            ivProfile.setImageURI(uri)
                Log.d("uri", "onActivityResult: " + uri)
            profileImage = File(uri.path)
            profileImage=mCompressor!!.compressToFile(profileImage)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }


    }

    private fun update() {

        val progressDialog = ProgressDialog(this)
        //  progressDialog.setTitle("Kotlin Progress Bar")
        progressDialog.setMessage("Loading...")
        progressDialog.show()

//        Log.d("uri", "onActivityResult: ===>" + doc1.toString())

        val userId: RequestBody? = SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.USER_ID, "")?.let {
            RequestBody.create(
                MultipartBody.FORM, it
            )
        }

        val fName: RequestBody? = fName?.let {
            RequestBody.create(
                MultipartBody.FORM, it
            )
        }
        val lName: RequestBody? = lName?.let {
            RequestBody.create(
                MultipartBody.FORM, it
            )
        }
        val email: RequestBody? = email?.let {
            RequestBody.create(
                MultipartBody.FORM, it
            )
        }
        

        val multiPartRepeatString = "application/image"

        var profile_image: MultipartBody.Part? = null
        if (profileImage != null) {
            //  val file = File(isProfileUri!!.path)
            val file = profileImage
            val signPicBody =
                file?.let { RequestBody.create(multiPartRepeatString.toMediaTypeOrNull(), it) }
            profile_image =
                signPicBody?.let {
                    MultipartBody.Part.createFormData("profile_photo", file.name,
                        it
                    )
                }
        }
       
     
        //Log.d("uri", "onActivityResult: ========>after part" + doc1.toString())

        val apiCall = ApiInterface.create().editDocument(
            userId,
            fName,
            lName,
            email,
            profile_image,
        )

        apiCall.enqueue(object : Callback<UpdateProfileResponse> {
            override fun onResponse(
                call: Call<UpdateProfileResponse>,
                response: Response<UpdateProfileResponse>
            ) {
                progressDialog.dismiss()
                if (response.isSuccessful) {

                    if (response.body()!!.success) {

                        SharedPreferenceUtils.getInstance(this@ProfileActivity)
                            ?.setStringValue(
                                ConstantUtils.FIRSTNAME,
                                this@ProfileActivity.fName
                            )

                        SharedPreferenceUtils.getInstance(this@ProfileActivity)
                            ?.setStringValue(
                                ConstantUtils.LASTNAME,
                                this@ProfileActivity.lName
                            )

                        SharedPreferenceUtils.getInstance(this@ProfileActivity)
                            ?.setStringValue(
                                ConstantUtils.EMAIL_ID,
                                this@ProfileActivity.email
                            )

                        SharedPreferenceUtils.getInstance(this@ProfileActivity)
                            ?.setStringValue(
                                ConstantUtils.PROFILE_IMAGE,
                                response.body()!!.data[0].profile_photo
                            )
                        Toast.makeText(
                            applicationContext,
                            response.body()!!.msg,
                            Toast.LENGTH_SHORT
                        ).show()
                        onBackPressed()




                    } else {
                        Toast.makeText(
                            applicationContext,
                            response.body()!!.msg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }


            }

            override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, t.toString(), Toast.LENGTH_SHORT).show()
            }


        })


    }


}
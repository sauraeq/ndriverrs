package com.app.whirl.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.app.whirl.R
import com.app.whirl.activities.otp.OTPActivity
import com.app.whirl.network.ApiInterface
import com.app.whirl.network.LoginApiResponse
import com.app.whirl.network.SignupApiResponse
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        imageView.setOnClickListener {
            onBackPressed()
        }
    }

    fun click(view: View) {

    }

    fun clickNext(view: View) {

        if (isValidate()) {
            signupApiCall()
        }
     }

    private fun isValidate(): Boolean {

        if (edtPhoneNo.text.toString().trim().length < 10) {
            Toast.makeText(applicationContext, "Please enter valid phone no", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        return true;
    }

    private fun signupApiCall() {
        val progressDialog = ProgressDialog(this)
    //    progressDialog.setTitle("Kotlin Progress Bar")
        progressDialog.setMessage("Loading...")
        progressDialog.show()



        var hashMap: HashMap<String, String> = HashMap<String, String>()
        hashMap.put("mobile", edtPhoneNo.text.toString().trim())
        hashMap.put("device_tokan", "dfdsf")
        val apiCall = ApiInterface.create().driverSignup(hashMap)

        apiCall.enqueue(object : Callback<SignupApiResponse> {
            override fun onResponse(
                call: Call<SignupApiResponse>,
                response: Response<SignupApiResponse>
            ) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    if (response.body()!!.success) {
                        Toast.makeText(
                            applicationContext,
                            response.body()!!.data.otp,
                            Toast.LENGTH_SHORT
                        ).show()
                        moveToActivity()

                    } else {
                        Toast.makeText(
                            applicationContext,
                            response.body()!!.msg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }else{
                    Toast.makeText(
                        applicationContext,
                       resources.getString(R.string.servererror),
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }

            override fun onFailure(call: Call<SignupApiResponse>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    t.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            progressDialog.dismiss()
            }


        })

    }

    private fun moveToActivity() {

        val intent = Intent(this, OTPActivity::class.java)
        intent.putExtra("number", edtPhoneNo.text.toString().trim())
        intent.putExtra("isLogin", false)
        startActivity(intent)




//        val intent = Intent(this, OTPActivity::class.java)
//        intent.putExtra("number", edtPhoneNo.text.toString().trim())
//        intent.putExtra("isLogin", false)
//        startActivity(intent)
    }
}
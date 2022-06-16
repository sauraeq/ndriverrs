package com.app.whirl.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.app.whirl.HomeActivity
import com.app.whirl.R
import com.app.whirl.activities.base.BaseActivity
import com.app.whirl.activities.otp.OTPActivity
import com.app.whirl.model.Login
import com.app.whirl.network.ApiInterface
import com.app.whirl.network.LoginApiResponse
import com.app.whirl.utils.NetworkUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun click(view: View) {
        if (isValidate()) {
            if(NetworkUtils.checkInternetConnection(this)) {
                otpApiCall()
            }
        }
        // finish()
    }



    fun onClickBack(view: View) {
        onBackPressed()
    }

    fun onClickRegister(view: View) {

        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)

        // Toast.makeText(this,"Under Development",Toast.LENGTH_SHORT).show()

    }

    fun clickNext(view: View) {
        if (isValidate()) {
            val intent = Intent(this, RegisterTwoActivity::class.java)
            intent.putExtra("number", edtPhoneNo.text.toString().trim())
            startActivity(intent)
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

    fun otpApiCall() {


        rlLoader.visibility=View.VISIBLE

        var hashMap: HashMap<String, String> = HashMap<String, String>()
        hashMap.put("mobile", edtPhoneNo.text.toString().trim())
        hashMap.put("device_tokan", "dfdsf")

        val apiCall = ApiInterface.create().loginOtp(hashMap)

        apiCall.enqueue(object : Callback<LoginApiResponse> {
            override fun onResponse(call: Call<LoginApiResponse>, response: Response<LoginApiResponse>) {
                //TODO("Not yet implemented")

              //  if (response.isSuccessful) {

                Log.d("onResponse", "onResponse: "+response.body()!!.success)
                    if (response.body()!!.error==0) {
                      /*  Toast.makeText(
                            applicationContext,
                            response.body()!!.msg,
                            Toast.LENGTH_SHORT
                        ).show()*/
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

                //}

                rlLoader.visibility=View.GONE
            }

            override fun onFailure(call: Call<LoginApiResponse>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    t.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                rlLoader.visibility=View.GONE
            }


        })
    }

    private fun moveToActivity() {

        val intent = Intent(applicationContext, OTPActivity::class.java)
        intent.putExtra("number", edtPhoneNo.text.toString().trim())
        intent.putExtra("isLogin", true)
        startActivity(intent)

    }

}
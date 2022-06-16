package com.app.whirl.activities.otp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.app.whirl.HomeActivity
import com.app.whirl.R
import com.app.whirl.activities.RegisterTwoActivity
import com.app.whirl.activities.UploadProfileActivity
import com.app.whirl.activities.otp.OtpVerifyModel.OtpVerifyResponse
import com.app.whirl.model.Login
import com.app.whirl.network.ApiInterface
import com.app.whirl.network.LoginApiResponse
import com.app.whirl.network.OtpApiResponse
import com.app.whirl.network.SignupApiResponse
import com.app.whirl.utils.NetworkUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

import com.metaled.Utils.ConstantUtils
import com.metaled.Utils.SharedPreferenceUtils
import kotlinx.android.synthetic.main.activity_otpactivity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OTPActivity : AppCompatActivity() {

    val start = 120000L
    var timer = start
    lateinit var countDownTimer: CountDownTimer

    var  isResend:Boolean=false
    var isLogin: Boolean=false
    var number:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpactivity)
        getData()
        //setTextTimer()
        startCountDown()

        edtVerificationFirstColumn.addTextChangedListener(textWatcher1)
        edt_verification_second_column.addTextChangedListener(textWatcher2)
        edtVerificationThirdColumn.addTextChangedListener(textWatcher3)
        edtVerificationForthColumn.addTextChangedListener(textWatcher4)
        edtVerificationFifthColumn.addTextChangedListener(textWatcher5)
        imageView.setOnClickListener {
            onBackPressed()
        }
    }

    private fun startCountDown() {

            countDownTimer = object : CountDownTimer(timer,1000){
                //            end of timer
                override fun onFinish() {
                    isResend=true
                    Toast.makeText(applicationContext,"end timer",Toast.LENGTH_SHORT).show()
                }

                override fun onTick(millisUntilFinished: Long) {
                    isResend=false
                    timer = millisUntilFinished
                    setTextTimer()

                }

            }.start()


    }
    //  timer format
    fun setTextTimer() {
        var m = (timer / 1000) / 60
        var s = (timer / 1000) % 60

        var format = String.format("%02d:%02d", m, s)

        tvCounter.setText(format)
    }

    private fun getData() {
        number = intent.getStringExtra("number").toString()
        isLogin=intent.getBooleanExtra("isLogin",false)
        tvMobile.setText("Enter the 6-digit code sent to you at "+number)
    }

    fun onClickBack(view: View) {
        onBackPressed()
    }


    fun onClickNext(view: View) {

        if(isValidate()) {
            if(NetworkUtils.checkInternetConnection(this)) {
                verifyOtp()
            }
        }
    }

    private fun isValidate(): Boolean {
        //TODO("Not yet implemented")

        if(getOtpData().length<6) {
         Toast.makeText(applicationContext,"Please enter all fields",Toast.LENGTH_SHORT).show()
            return false;
        }

    return  true
    }

    private fun getOtpData():String {
        var otp:String=""

        otp=edtVerificationFirstColumn.text.toString().trim()+
                edt_verification_second_column.text.toString().trim()+
                edtVerificationThirdColumn.text.toString().trim()+
                edtVerificationForthColumn.text.toString().trim()+
                edtVerificationFifthColumn.text.toString().trim()+
                edtVerificationSixColumn.text.toString().trim()


        return otp;
    }

    private fun verifyOtp() {
       // getOtpData()
         rlLoader.visibility=View.VISIBLE
        var hashMap: HashMap<String, String> = HashMap<String, String>()
        hashMap.put("mobile", number)
        hashMap.put("otp",getOtpData())


        val apiCall = ApiInterface.create().otpVerify(hashMap)

        apiCall.enqueue(object : Callback<OtpVerifyResponse> {
            override fun onResponse(call: Call<OtpVerifyResponse>, response: Response<OtpVerifyResponse>) {

                if(response.body()!!.success){
                 /*  AppPref.getInstance(applicationContext)!!.savePersonDetails(response.body()!!.data)
                    AppPref.getInstance(applicationContext)!!.isLogin=true*/

                    SharedPreferenceUtils.getInstance(this@OTPActivity)
                        ?.setStringValue(ConstantUtils.USER_MOBILE_No,response.body()!!.data.mobile)
                    SharedPreferenceUtils.getInstance(this@OTPActivity)
                        ?.setStringValue(ConstantUtils.USER_ID,response.body()!!.data.id)

                     if(!response.body()!!.data.step.isNullOrEmpty()){
                         if(response.body()!!.data.step.equals("1")){
                             SharedPreferenceUtils.getInstance(this@OTPActivity)
                                 ?.setStringValue(ConstantUtils.IS_DOCUMENTATION, "true")
                         } else{
                             SharedPreferenceUtils.getInstance(this@OTPActivity)
                                 ?.setStringValue(ConstantUtils.IS_DOCUMENTATION, "false")
                         }
                     }else{
                         SharedPreferenceUtils.getInstance(this@OTPActivity)
                             ?.setStringValue(ConstantUtils.IS_DOCUMENTATION, "false")
                     }
                    SharedPreferenceUtils.getInstance(this@OTPActivity)
                        ?.setStringValue(ConstantUtils.IS_LOGIN,"true")

                    moveToActivity(response)

                }else{
                    Toast.makeText(applicationContext,response.body()!!.msg,Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<OtpVerifyResponse>, t: Throwable) {
                Toast.makeText(applicationContext,t.toString(),Toast.LENGTH_SHORT).show()
                rlLoader.visibility=View.GONE
            }


        })

    }

    private fun moveToActivity(response:Response<OtpVerifyResponse>) {

        if(isLogin){
         loginFireBase(response)

        }else{
            rlLoader.visibility=View.GONE
            val intent = Intent(this, RegisterTwoActivity::class.java)
           // intent.putExtra("number", number)
            startActivity(intent)
            finishAffinity()
        }
    }

    private fun signupApiCall() {
        var  hashMap:  HashMap<String,String> = HashMap<String,String>()
        hashMap.put("mobile",number)
        hashMap.put("device_tokan", "")

        val apiCall = ApiInterface.create().driverSignup(hashMap)

        apiCall.enqueue(object : Callback<SignupApiResponse> {
            override fun onResponse(call: Call<SignupApiResponse>, response: Response<SignupApiResponse>) {
                //TODO("Not yet implemented")
                if(response.isSuccessful){

                    if(response.body()!!.success){
                        Toast.makeText(applicationContext,response.body()!!.msg,Toast.LENGTH_SHORT).show()

                    }else{
                        Toast.makeText(applicationContext,response.body()!!.msg,Toast.LENGTH_SHORT).show()
                    }

                }


            }

            override fun onFailure(call: Call<SignupApiResponse>, t: Throwable) {
                //  TODO("Not yet implemented")
            }


        })



    }

    fun onClickResendOtp(view: View) {
        if (isResend) {
            startCountDown()
            resendOtp()
        }
    }

    private fun resendOtp() {

        var hashMap: HashMap<String, String> = HashMap<String, String>()
        hashMap.put("mobile", number)

        val apiCall = ApiInterface.create().driverResendOtp(hashMap)
        apiCall.enqueue(object : Callback<SignupApiResponse> {
            override fun onResponse(
                call: Call<SignupApiResponse>,
                response: Response<SignupApiResponse>
            ) {
                //TODO("Not yet implemented")
                if (response.isSuccessful) {

                    if (response.body()!!.success) {
                        Toast.makeText(
                            applicationContext,
                            response.body()!!.msg,
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        Toast.makeText(
                            applicationContext,
                            response.body()!!.msg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }


            }

            override fun onFailure(call: Call<SignupApiResponse>, t: Throwable) {
                //  TODO("Not yet implemented")
            }


        })

    }

    private val textWatcher1 = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (start == 0) {
                edt_verification_second_column.requestFocus();

            }
        }
    }

    private val textWatcher2 = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (start == 0) {
                edtVerificationThirdColumn.requestFocus();

            }
        }
    }
    private val textWatcher3 = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (start == 0) {
                edtVerificationForthColumn.requestFocus();

            }
        }
    }

    private val textWatcher4 = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (start == 0) {
                edtVerificationFifthColumn.requestFocus();

            }
        }
    }

    private val textWatcher5 = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (start == 0) {
                edtVerificationSixColumn.requestFocus();

            }
        }
    }

    fun loginFireBase(response:Response<OtpVerifyResponse>){
        Log.d("loginFireBase", "loginFireBase")
        FirebaseAuth.getInstance().signInWithEmailAndPassword(response.body()!!.data.mobile+"@gamil.com", response.body()!!.data.mobile)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                Log.d("response", task.toString())
                if (task.isSuccessful) {





                    if(!response.body()!!.data.fname.isNullOrEmpty()){
                        SharedPreferenceUtils.getInstance(this@OTPActivity)
                            ?.setStringValue(ConstantUtils.FIRSTNAME,response.body()!!.data.fname)
                    }
                    if(!response.body()!!.data.lname.isNullOrEmpty()){
                        SharedPreferenceUtils.getInstance(this@OTPActivity)
                            ?.setStringValue(ConstantUtils.LASTNAME,response.body()!!.data.lname)
                    }

                    if(!response.body()!!.data.email.isNullOrEmpty()){
                        SharedPreferenceUtils.getInstance(this@OTPActivity)
                            ?.setStringValue(ConstantUtils.EMAIL_ID,response.body()!!.data.email)
                    }
                    if(!response.body()!!.data.email.isNullOrEmpty()){
                        SharedPreferenceUtils.getInstance(this@OTPActivity)
                            ?.setStringValue(ConstantUtils.PROFILE_IMAGE,response.body()!!.data.profile_photo)
                    }
                    SharedPreferenceUtils.getInstance(this@OTPActivity)
                        ?.setValue(ConstantUtils.REGISTRATION_STEP,0)
                    rlLoader.visibility=View.GONE
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finishAffinity()


                } else {
                    rlLoader.visibility=View.GONE
                    Toast.makeText(this, "Authentication failed!", Toast.LENGTH_SHORT)
                        .show()
                }
            })

    }


}
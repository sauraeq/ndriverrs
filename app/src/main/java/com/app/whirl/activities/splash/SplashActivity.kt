package com.app.whirl.activities.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.app.whirl.HomeActivity
import com.app.whirl.MainActivity
import com.app.whirl.R
import com.app.whirl.activities.RegisterTwoActivity
import com.app.whirl.activities.base.BaseActivity
import com.app.whirl.activities.started.GetStartedActivity

import com.metaled.Utils.ConstantUtils
import com.metaled.Utils.SharedPreferenceUtils

class SplashActivity : BaseActivity() {

    val TIME_OUT:Long=3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        loadSplashScreen()
    }

    private fun loadSplashScreen(){
        Handler().postDelayed({
            if(SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.IS_LOGIN,"false").equals("true")){
               // if(SharedPreferenceUtils.getInstance(this)?.getIntValue(ConstantUtils.REGISTRATION_STEP,0)!=0){

                   if(SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.IS_DOCUMENTATION,"false").equals("true")){
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }else{
                    val intent = Intent(this, RegisterTwoActivity::class.java)
                    intent.putExtra("number", SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.USER_MOBILE_No,""))
                    intent.putExtra("isLogin", false)
                    startActivity(intent)
                }

            }else {
                val intent = Intent(this, GetStartedActivity::class.java)
                startActivity(intent)
            }
            finish()
        },TIME_OUT)
    }
}
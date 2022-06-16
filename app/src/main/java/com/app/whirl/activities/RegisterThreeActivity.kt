package com.app.whirl.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.app.whirl.R
import com.app.whirl.utils.Utils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register_three.*
import kotlinx.android.synthetic.main.activity_register_two.*

class RegisterThreeActivity : AppCompatActivity() {

    var fName: String = ""

    var lName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_three)
        getData()
        imageViewback.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getData() {
        fName = intent.getStringExtra("fname").toString()
        lName = intent.getStringExtra("lname").toString()
    }

    fun click(view: View) {
        if(isValidate()) {
            val intent = Intent(this, SelectVehicleTypeActivity::class.java)
            intent.putExtra("fname", fName)
            intent.putExtra("lname", lName)
            intent.putExtra("email", edtEmail.text.toString().trim())
            startActivity(intent)
        }
    }

    private fun isValidate(): Boolean {
        if(edtEmail.text.toString().trim().isEmpty()){
            Utils.showToast(applicationContext,"Please enter email address")
            return false
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmail.text.toString().trim()).matches()){
            Utils.showToast(applicationContext,"Please enter  valid email address")
            return false
        }
        return true
    }
}
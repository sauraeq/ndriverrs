package com.app.whirl.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.app.whirl.R
import com.app.whirl.utils.Utils
import com.metaled.Utils.ConstantUtils
import com.metaled.Utils.SharedPreferenceUtils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_otpactivity.*
import kotlinx.android.synthetic.main.activity_register_two.*

import kotlinx.android.synthetic.main.activity_otpactivity.*

class RegisterTwoActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_two)

        imageView2.setOnClickListener {
            onBackPressed()
        }
    }


    fun clickNext(view: View) {
        if (isValidate()) {

            val intent = Intent(this, RegisterThreeActivity::class.java)
            intent.putExtra("fname", edtFirstName.text.toString().trim())
            intent.putExtra("lname", edtLastName.text.toString().trim())

            startActivity(intent)
        }

    }

    private fun isValidate(): Boolean {

        if (edtFirstName.text.toString().trim().isEmpty()) {
            Utils.showToast(applicationContext, "Please enter first name")
            return false
        } else if (edtLastName.text.toString().trim().isEmpty()) {
            Utils.showToast(applicationContext, "Please enter last name")
            return false
        }
        return true
    }
}
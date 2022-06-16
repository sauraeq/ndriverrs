package com.app.whirl.activities.started

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.app.whirl.R
import com.app.whirl.activities.LoginActivity
import com.app.whirl.activities.base.BaseActivity

class GetStartedActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)
    }

    fun click(view: View) {

        val intent = Intent(this, LoginActivity ::class.java)
        startActivity(intent)
       // finish()
    }
}
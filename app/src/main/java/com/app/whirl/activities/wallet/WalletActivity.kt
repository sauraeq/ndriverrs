package com.app.whirl.activities.wallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.whirl.R
import com.metaled.HomeModule.Adapters.BestSellerAdapter
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.my_toolbar
import kotlinx.android.synthetic.main.activity_your_earned.*
import androidx.recyclerview.widget.DividerItemDecoration




class WalletActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)

        my_toolbar_earned.setTitle("")

        my_toolbar_earned.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24)

        my_toolbar_earned.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                onBackPressed()
            }
        })


    }

}
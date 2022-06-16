package com.app.whirl.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.app.whirl.HomeActivity
import com.app.whirl.R
import com.app.whirl.network.ApiInterface
import com.app.whirl.ui.home.AcceptTripModels.AcceptTripResponse
import com.app.whirl.utils.NetworkUtils
import com.app.whirl.utils.ToastUtil
import com.example.ranierilavastone.Utils.StringUtil
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.activity_payment.rlLoader
import kotlinx.android.synthetic.main.activity_payment.tvDropAddress
import kotlinx.android.synthetic.main.activity_payment.tvName
import kotlinx.android.synthetic.main.activity_payment.tvPickAddress
import kotlinx.android.synthetic.main.payment__popup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Payment_Activity : AppCompatActivity() {
   var booking_id=""
   var custName=""
   var pickupAddress=""
   var dropAddress=""
   var paymentStatus=""
   var tripPrice=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        booking_id=intent.getStringExtra("booking_id").toString()
        custName=intent.getStringExtra("custName").toString()
        pickupAddress=intent.getStringExtra("pickupAddress").toString()
        dropAddress=intent.getStringExtra("dropAddress").toString()
        paymentStatus=intent.getStringExtra("paymentStatus").toString()
        tripPrice=intent.getStringExtra("tripPrice").toString()
        tvAmount.setText("$"+tripPrice)
        tvName.setText(StringUtil.capString(custName))
        tvPickAddress.setText(pickupAddress)
        tvDropAddress.setText(dropAddress)

        if(paymentStatus.equals("0")){
            rbPaymentStatus.isChecked=true
        }

        tvSubmit.setOnClickListener {
          if(NetworkUtils.checkInternetConnection(this)){
              completeTrip()
          }
        }
        tvOk.setOnClickListener {
            onBackPressed()

        }

    }
    private fun completeTrip() {
        rlLoader.visibility = View.VISIBLE
        var hashMap: HashMap<String, String> = HashMap<String, String>()
        hashMap.put("driver_id", StringUtil.getUserId(this))
        hashMap.put("booking_id", booking_id)

        val apiCall = ApiInterface.create().completeTrip(hashMap)
        apiCall.enqueue(object : Callback<AcceptTripResponse> {
            override fun onResponse(
                call: Call<AcceptTripResponse>,
                response: Response<AcceptTripResponse>
            ) {
                rlLoader.visibility = View.GONE
                if (response.isSuccessful) {
                    if (response.body()!!.success) {
                        rlPaymentPopup.visibility=View.VISIBLE
                        ToastUtil.toast_Long(this@Payment_Activity, response.body()!!.msg)

                    } else {

                        ToastUtil.toast_Long(this@Payment_Activity, response.body()!!.msg)

                    }

                } else {
                    ToastUtil.toast_Long(this@Payment_Activity, resources.getString(R.string.servererror))
                }


            }

            override fun onFailure(call: Call<AcceptTripResponse>, t: Throwable) {
                rlLoader.visibility = View.GONE
                ToastUtil.toast_Long(this@Payment_Activity, t.toString())


            }


        })

    }

    override fun onBackPressed() {
        if(rlPaymentPopup.visibility==View.VISIBLE){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }else{
            super.onBackPressed()
        }

    }

}
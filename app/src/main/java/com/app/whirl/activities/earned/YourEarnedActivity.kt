package com.app.whirl.activities.earned

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
import com.app.whirl.activities.earned.Adapters.EarningAdapter
import com.app.whirl.activities.earned.Models.Data
import com.app.whirl.activities.earned.Models.EarnResponse
import com.app.whirl.activities.notification.Adapters.NotificationAdapterNew
import com.app.whirl.activities.notification.NotificationModels.NotificationResponse
import com.app.whirl.network.ApiInterface
import com.app.whirl.utils.NetworkUtils
import com.example.ranierilavastone.Utils.StringUtil

import kotlinx.android.synthetic.main.activity_your_earned.rlLoader
import kotlinx.android.synthetic.main.activity_your_earned.rvList
import kotlinx.android.synthetic.main.activity_your_earned.tvNorecord
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class YourEarnedActivity : AppCompatActivity() {
    lateinit var list:ArrayList<Data>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_earned)
        list= ArrayList()
        if(NetworkUtils.checkInternetConnection(this)){
            getEarning()
        }else{
            tvNorecord.visibility=View.VISIBLE
        }
    }


    private fun getEarning() {
        rlLoader.visibility = View.VISIBLE
        var hashMap: HashMap<String, String> = HashMap<String, String>()
        hashMap.put("driver_id", StringUtil.getUserId(this))


        val apiCall = ApiInterface.create().getEarnList(hashMap)
        apiCall.enqueue(object : Callback<EarnResponse> {
            override fun onResponse(
                call: Call<EarnResponse>,
                response: Response<EarnResponse>
            ) {
                rlLoader.visibility = View.GONE
                if (response.isSuccessful) {
                    if (response.body()!!.success) {
                        if(response.body()!!.data!=null){
                            list=response.body()!!.data
                            setList()
                        }else{
                            setList()
                        }



                    } else {
                        setList()

                    }

                } else {
                    setList()

                }


            }

            override fun onFailure(call: Call<EarnResponse>, t: Throwable) {
                rlLoader.visibility = View.GONE
                setList()


            }


        })

    }
    fun setList(){
        if(list.size>0){
            tvNorecord.visibility=View.GONE
        }else{
            tvNorecord.visibility=View.VISIBLE
        }
        rvList.layoutManager = LinearLayoutManager(this)

        rvList.layoutManager=
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        rvList.adapter= EarningAdapter(this, list)
    }
}
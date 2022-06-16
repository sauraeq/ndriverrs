package com.app.whirl


import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.ui.AppBarConfiguration
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.app.whirl.activities.InterfaceName
import com.app.whirl.activities.LoginActivity
import com.app.whirl.activities.changepassword.ChangePasswordActivity
import com.app.whirl.activities.earned.YourEarnedActivity
import com.app.whirl.activities.notification.NotificationActivity
import com.app.whirl.activities.profile.ProfileActivity
import com.app.whirl.activities.setting.SettingActivity
import com.app.whirl.activities.wallet.WalletActivity
import com.app.whirl.databinding.ActivityHomeBinding
import com.app.whirl.network.ApiInterface
import com.app.whirl.ui.home.HomeFragment
import com.app.whirl.ui.home.LocationModels.LocationUpdateResponse
import com.app.whirl.ui.home.UpdateTokenModels.UpdateFCMTokenResponse
import com.app.whirl.utils.NetworkUtils
import com.app.whirl.utils.ToastUtil
import com.example.ranierilavastone.Utils.StringUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.metaled.Utils.ConstantUtils
import com.metaled.Utils.SharedPreferenceUtils
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.nav_header_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity(), View.OnClickListener,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    lateinit var lintener: InterfaceName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        generateFCMtoken()

        lintener=HomeFragment()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarHome.toolbar)

        binding.appBarHome.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        loadFragment(HomeFragment())
        if(SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.DRIVER_STATUS,"1").equals("2")){
            switchs.isChecked=true
            tvStatus.setText("Online")
        }else{
            switchs.isChecked=false
            tvStatus.setText("Offline")
        }

        binding.navView.setNavigationItemSelectedListener(this)
        navigation_icon.setOnClickListener {
            openDrawer()
        }
        switchs.setOnCheckedChangeListener { compoundButton, isChecked ->
            when (isChecked) {
                true -> {
                    tvStatus.setText("Online")
                    val fragment: HomeFragment? =
                        supportFragmentManager.findFragmentById(R.id.framelayout) as HomeFragment?
                    fragment?.on()
                    // (fragmentManager as HomeFragment?)?.data("online")
                    }
                false -> {
                    tvStatus.setText("Offline")
                    val fragment: HomeFragment? =
                        supportFragmentManager.findFragmentById(R.id.framelayout) as HomeFragment?
                    fragment?.off()
                    //lintener.onClick("Offline")
                   /* val fragment: HomeFragment? =
                        supportFragmentManager.findFragmentById(R.id.nav_home) as HomeFragment?
                    if (fragment != null) {
                        fragment.data1()
                    }else{
                        fragment?.data1()
                    }*/
                }
            }
        }

    }
    private fun generateFCMtoken() {
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(
                        ContentValues.TAG,
                        "Fetching FCM registration token failed",
                        task.exception
                    )
                    return@OnCompleteListener
                }
                val token = task.result
                SharedPreferenceUtils.getInstance(this)
                    ?.setStringValue(ConstantUtils.FCM_TOKEN, token)
                Log.d("FCMToken", token)
                updateToKen(token)
            })
        }catch (e:Exception){

        }
    }
    private fun updateToKen(token:String) {

        var hashMap: HashMap<String, String> = HashMap<String, String>()
        hashMap.put("id", StringUtil.getUserId(this))
        hashMap.put("type", "driver_ragistration")
        hashMap.put("device_tokan",token)

        val apiCall = ApiInterface.create().updateFcmToken(hashMap)
        apiCall.enqueue(object : Callback<UpdateFCMTokenResponse> {
            override fun onResponse(
                call: Call<UpdateFCMTokenResponse>,
                response: Response<UpdateFCMTokenResponse>
            ) {
                rlLoader.visibility = View.GONE
                if (response.isSuccessful) {


                } else {

                }


            }

            override fun onFailure(call: Call<UpdateFCMTokenResponse>, t: Throwable) {



            }


        })

    }

    override fun onStart() {
        super.onStart()
        //tvName.setText(SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.FIRSTNAME,"")+" "+SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.LASTNAME,""))
       // tvMobileNo.setText(SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.USER_MOBILE_No,""))

    }
    fun offSwitchButton(){
        switchs.isChecked=false
    }

    fun loadFragment(Fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.framelayout, Fragment)
        transaction.commit()
    }

    private fun openDrawer() {
        if (!drawer_layout.isDrawerOpen(binding.navView)) {
            drawer_layout.openDrawer(binding.navView)
        } else {
            drawer_layout.closeDrawer(binding.navView)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                val intent = Intent(this, ProfileActivity ::class.java)
                startActivity(intent)
                drawer_layout.closeDrawer(binding.navView)
            }
            R.id.nav_your_earned -> {
                val intent = Intent(this, YourEarnedActivity ::class.java)
                startActivity(intent)
                drawer_layout.closeDrawer(binding.navView)
            }
            R.id.nav_my_wallet -> {
                val intent = Intent(this, WalletActivity ::class.java)
                startActivity(intent)
                drawer_layout.closeDrawer(binding.navView)
            }
            R.id.nav_notification -> {
                val intent = Intent(this, NotificationActivity ::class.java)
                startActivity(intent)
                drawer_layout.closeDrawer(binding.navView)
            }
            R.id.nav_change_password -> {
                val intent = Intent(this, ChangePasswordActivity ::class.java)
                startActivity(intent)
                drawer_layout.closeDrawer(binding.navView)
            }
            R.id.nav_settings -> {
                val intent = Intent(this, SettingActivity ::class.java)
                startActivity(intent)
                drawer_layout.closeDrawer(binding.navView)
            }


            R.id.nav_logout -> {
                SharedPreferenceUtils.getInstance(this)?.clear()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finishAffinity()
                drawer_layout.closeDrawer(binding.navView)
            }
        }
        return false
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }


    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onClick(p0: View?) {


    }
}
package com.app.whirl.ui.home


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.app.whirl.R
import com.app.whirl.Services.ServiceTaskNew
import com.app.whirl.activities.InterfaceName
import com.app.whirl.network.ApiInterface
import com.app.whirl.ui.home.AcceptTripModels.AcceptTripResponse
import com.app.whirl.ui.home.DriverTripModels.DriverTripResponse
import com.app.whirl.ui.home.LocationModels.LocationUpdateResponse
import com.app.whirl.ui.home.OffLineDetailModels.OffLineDetailResponse
import com.app.whirl.utils.NetworkUtils
import com.app.whirl.utils.PermissionUtils
import com.app.whirl.utils.ToastUtil
import com.bumptech.glide.Glide
import com.example.ranierilavastone.Utils.StringUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.metaled.Utils.ConstantUtils
import com.metaled.Utils.SharedPreferenceUtils
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.otp_verify_popup.*
import kotlinx.android.synthetic.main.start_ride_popup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment(), InterfaceName {


    lateinit var button_offline: CardView
    lateinit var card_online: LinearLayout


    lateinit var on: Button
    lateinit var off: Button
    var booking_id = ""
    var pickup_latitude = ""
    var pickup_long = ""

    var drop_latitude = ""
    var drop_long = ""
    var bookingTime = ""
    var bookingDis = ""
    var custProfile = ""
    var custMobileno = ""
    var bookingotp = ""
    var pickupAddress = ""
    var dropAddress = ""
    var custName = ""
    var paymentStatus = "0"
    var tripPrice = ""


    private val callback = OnMapReadyCallback { googleMap ->
        val sydney = LatLng(28.6201891, 77.342835)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Sector 62 Noida"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootview = inflater.inflate(R.layout.fragment_home, container, false)

        button_offline = rootview.findViewById(R.id.button_offline)
        card_online = rootview.findViewById(R.id.card_online)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment!!.getMapAsync { mMap ->


            val googlePlex = CameraPosition.builder()
                .target(LatLng(28.6201891, 77.342835))
                .zoom(10f)
                .tilt(45f)
                .build()

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 3000, null)
            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(28.6201891, 77.342835))
                    .title("Spider Man")
                    .icon(
                        bitmapDescriptorFromVector(
                            activity,
                            R.drawable.ic_baseline_location_on_24_orange
                        )
                    )
            )

        }



        return rootview
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOtp()


        PermissionUtils.checkAndRequestPermissions(requireActivity())
        var serviceIntent = Intent(activity, ServiceTaskNew::class.java)
        activity?.startService(serviceIntent)

        tvName.setText(
            SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(
                ConstantUtils.FIRSTNAME,
                ""
            )?.let { StringUtil.capString(it) } + " " + SharedPreferenceUtils.getInstance(
                requireContext()
            )?.getStringValue(ConstantUtils.LASTNAME, "")
        )
        onClick()



        updateDriverLocation()
        getOffLineDetail()

    }

    override fun onStart() {
        super.onStart()
        checkGPSLocationStatus()
    }
    fun checkGPSLocationStatus() {
        val manager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        if (!manager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }
    }


    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(requireContext())

        builder.setMessage("Your GPS seems to be disabled, Please enable it.")
            .setCancelable(false)
            .setPositiveButton(
                "OK"
            ) { dialog, id ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
        val alert = builder.create()
        alert.show()
    }
    fun setOtp() {
        etCode1.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length > 0) {
                    etCode2.requestFocus()
                }
            }
        })

        etCode2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (p0.toString().length > 0) {
                    etCode3.requestFocus()
                }
            }
        })

        etCode3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (p0.toString().length > 0) {
                    etCode4.requestFocus()
                }
            }
        })
      /*  etCode4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (p0.toString().length > 0) {
                    etCode5.requestFocus()
                }
            }
        })*/

        etCode2.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                etCode2.setText("")
                etCode1.requestFocus()
            }
            false
        }

        etCode3.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                etCode3.setText("")
                etCode2.requestFocus()
            }
            false
        }
        etCode4.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                etCode4.setText("")
                etCode3.requestFocus()
            }
            false
        }
      /*  etCode5.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                etCode5.setText("")
                etCode4.requestFocus()
            }
            false
        }
        etCode6.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                etCode6.setText("")
                etCode5.requestFocus()
            }
            false
        }*/

    }
    private fun bitmapDescriptorFromVector(context: Context?, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(requireContext(), vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap =
            Bitmap.createBitmap(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


    fun on() {

        SharedPreferenceUtils.getInstance(requireContext())
            ?.setStringValue(ConstantUtils.DRIVER_STATUS, "2")
        moon_offline.visibility = View.GONE
        updateDriverLocation()

    }

    fun off() {
        SharedPreferenceUtils.getInstance(requireContext())
            ?.setStringValue(ConstantUtils.DRIVER_STATUS, "1")
        moon_offline.visibility = View.VISIBLE
        updateDriverLocation()
    }

    fun onClick() {
        ivDirection.setOnClickListener {
            if (!pickup_latitude.isNullOrEmpty()) {
                //val gmmIntentUri = Uri.parse("google.navigation:q="+pickup_latitude+","+pickup_long + "&mode=d")
                val gmmIntentUri = Uri.parse("google.navigation:q=28.6921164,76.8111449&mode=d")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)

                /*   val navigation = Uri.parse("google.navigation:q=$pickup_latitude,$pickup_long")
                   val navigationIntent = Intent(Intent.ACTION_VIEW, navigation)
                   navigationIntent.setPackage("com.google.android.apps.maps")
                   startActivity(navigationIntent)
   */
                /* val gmmIntentUri: Uri = Uri.parse("geo:"+pickup_latitude+","+pickup_long)
                 val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                 mapIntent.setPackage("com.google.android.apps.maps")
                 if (activity?.let { it1 -> mapIntent.resolveActivity(it1.getPackageManager()) } != null) {
                     startActivity(mapIntent)
                 }*/
            }

        }
        tvBookingAccept.setOnClickListener {
            val builder1 = AlertDialog.Builder(requireContext())
            builder1.setMessage("Do you want to accept this booking?")
            builder1.setCancelable(true)
            builder1.setPositiveButton(
                "Yes"
            ) { dialog, id ->
                dialog.cancel()

                if (NetworkUtils.checkInternetConnection(activity)) {
                    acceptTrip()
                }
            }
            builder1.setNegativeButton(
                "No"
            ) { dialog, id -> dialog.cancel() }
            val alert11 = builder1.create()
            alert11.show()

        }
        tvBookingReject.setOnClickListener {
            val builder1 = AlertDialog.Builder(requireContext())
            builder1.setMessage("Do you want to reject this booking?")
            builder1.setCancelable(true)
            builder1.setPositiveButton(
                "Yes"
            ) { dialog, id ->
                dialog.cancel()

                if (NetworkUtils.checkInternetConnection(activity)) {
                    rejectTrip()
                }
            }
            builder1.setNegativeButton(
                "No"
            ) { dialog, id -> dialog.cancel() }
            val alert11 = builder1.create()
            alert11.show()

        }
        tvStart.setOnClickListener {
            rlOtpVerifyPopup.visibility=View.VISIBLE
        }
        tvRecall.setOnClickListener {
            if(!custMobileno.isNullOrEmpty()){
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:$custMobileno")
                startActivity(callIntent)
            }
        }
        ivCall.setOnClickListener {
         if(!custMobileno.isNullOrEmpty()){
             val callIntent = Intent(Intent.ACTION_DIAL)
             callIntent.data = Uri.parse("tel:$custMobileno")
             startActivity(callIntent)
         }
        }
        tvVerifyOtp.setOnClickListener {
            var otp=etCode1.text.toString()+""+etCode2.text.toString()+""+etCode3.text.toString()+""+etCode4.text.toString()
             if(otp.isNullOrEmpty()){
                 ToastUtil.toast_Long(activity,"Please enter OTP")
             }else if(otp.length!=4){
                 ToastUtil.toast_Long(activity,"Please enter 4 digit OTP")
             }else if(otp.length!=4){
                 ToastUtil.toast_Long(activity,"Please enter valid OTP")
             }else if(!otp.equals(bookingotp)){
                 ToastUtil.toast_Long(activity,"OTP does not match")
             }else{
                 rlOtpVerifyPopup.visibility=View.GONE
                 tvPickUserName.setText("Complete your Drive")

                 rlcall.visibility=View.GONE
                 llStart.visibility=View.GONE
                 tvComplete.visibility=View.VISIBLE

             }
        }
        tvComplete.setOnClickListener {
            if(NetworkUtils.checkInternetConnection(activity)){
                val intent = Intent(activity, Payment_Activity::class.java)
                intent.putExtra("bookingId", booking_id)
                intent.putExtra("custName", custName)
                intent.putExtra("pickupAddress", pickupAddress)
                intent.putExtra("dropAddress", dropAddress)
                intent.putExtra("paymentStatus", paymentStatus)
                intent.putExtra("tripPrice", tripPrice)
                startActivity(intent)

            }
        }

    }


    override fun onClick(name: String) {
        if (name.equals("Online")) {
            on()
        } else {
            off()

        }

    }

    private fun getOffLineDetail() {
        rlLoader.visibility = View.VISIBLE
        var hashMap: HashMap<String, String> = HashMap<String, String>()
        hashMap.put("driver_id", StringUtil.getUserId(requireContext()))

        val apiCall = ApiInterface.create().getOffLineDetail(hashMap)
        apiCall.enqueue(object : Callback<OffLineDetailResponse> {
            override fun onResponse(
                call: Call<OffLineDetailResponse>,
                response: Response<OffLineDetailResponse>
            ) {
                if (response.isSuccessful) {
                    rlLoader.visibility = View.GONE
                    if (response.body()!!.success) {

                        if (!response.body()!!.data[0].amount.isNullOrEmpty()) {
                            tvPrice.setText("$" + response.body()!!.data[0].amount)
                        }
                        if (!response.body()!!.data[0].time.isNullOrEmpty()) {
                            tvHr.setText(response.body()!!.data[0].amount)
                        }
                        if (!response.body()!!.data[0].distance.isNullOrEmpty()) {
                            tvDistance.setText(response.body()!!.data[0].distance + "Km")
                        }
                        if (!response.body()!!.data[0].total_ride.isNullOrEmpty()) {
                            tvJobs.setText(response.body()!!.data[0].total_ride)
                        }


                    } else {
                        rlLoader.visibility = View.GONE
                        ToastUtil.toast_Long(activity, response.body()!!.msg)
                    }

                }


            }

            override fun onFailure(call: Call<OffLineDetailResponse>, t: Throwable) {
                rlLoader.visibility = View.GONE
                ToastUtil.toast_Long(activity, t.toString())
            }


        })

    }

    private fun getDriverTripInfo() {
        rlLoader.visibility = View.VISIBLE
        var hashMap: HashMap<String, String> = HashMap<String, String>()
        hashMap.put("driver_id", StringUtil.getUserId(requireContext()))
        val apiCall = ApiInterface.create().getDriverTrip(hashMap)
        apiCall.enqueue(object : Callback<DriverTripResponse> {
            override fun onResponse(
                call: Call<DriverTripResponse>,
                response: Response<DriverTripResponse>
            ) {
                rlLoader.visibility = View.GONE
                if (response.isSuccessful) {
                    if (response.body()!!.success) {
                        if( response.body()!!.data.size>0) {
                            booking_id = response.body()!!.data[0].id
                            if (!response.body()!!.data[0].pickup_latitude.isNullOrEmpty()) {
                                pickup_latitude = response.body()!!.data[0].pickup_latitude
                            }
                            if (!response.body()!!.data[0].pickup_longitude.isNullOrEmpty()) {
                                pickup_long = response.body()!!.data[0].pickup_longitude
                            }
                            if (!response.body()!!.data[0].drop_latitude.isNullOrEmpty()) {
                                drop_latitude = response.body()!!.data[0].drop_latitude
                            }
                            if (!response.body()!!.data[0].drop_longitude.isNullOrEmpty()) {
                                drop_long = response.body()!!.data[0].drop_longitude
                            }
                            if (!response.body()!!.data[0].user_photo.isNullOrEmpty()) {
                                custProfile = response.body()!!.data[0].user_photo
                                Glide.with(activity).load(response.body()!!.data[0].user_photo)
                                    .placeholder(resources.getDrawable(R.drawable.profile_icon))
                                    .into(ivCustomerImage)
                                Glide.with(activity).load(response.body()!!.data[0].user_photo)
                                    .placeholder(resources.getDrawable(R.drawable.profile_icon))
                                    .into(ivCustProfile1)
                            }
                            if (!response.body()!!.data[0].amount.isNullOrEmpty()) {
                                tripPrice=response.body()!!.data[0].amount
                                tvBookingPrice.setText("$" + response.body()!!.data[0].amount)
                            }
                            if (!response.body()!!.data[0].user_name.isNullOrEmpty()) {
                                custName=response.body()!!.data[0].user_name
                                tvCustName.setText(response.body()!!.data[0].user_name)
                                tvPickUserName.setText("Picking up "+custName)
                            }
                            if (!response.body()!!.data[0].distance.isNullOrEmpty()) {
                                tvBookingDistance.setText(response.body()!!.data[0].distance)
                            }
                            if (!response.body()!!.data[0].pickup_address.isNullOrEmpty()) {
                                pickupAddress=response.body()!!.data[0].pickup_address
                                tvPickAddress.setText(response.body()!!.data[0].pickup_address)
                            }
                            if (!response.body()!!.data[0].drop_address.isNullOrEmpty()) {
                                dropAddress=response.body()!!.data[0].drop_address
                                tvDropAddress.setText(response.body()!!.data[0].drop_address)
                            }

                            if (!response.body()!!.data[0].time.isNullOrEmpty()) {
                                bookingTime = response.body()!!.data[0].time
                                tvTime.setText(bookingTime)
                            }
                            if (!response.body()!!.data[0].distance.isNullOrEmpty()) {
                                bookingDis = response.body()!!.data[0].distance
                                tvDistance2.setText(bookingDis)
                            }

                            if (!response.body()!!.data[0].user_phone.isNullOrEmpty()) {
                                custMobileno = response.body()!!.data[0].user_phone

                            }
                            if (!response.body()!!.data[0].otp.isNullOrEmpty()) {
                                bookingotp = response.body()!!.data[0].otp
                            }
                          /*  if (!response.body()!!.data[0].otp.isNullOrEmpty()) {
                                bookingotp = response.body()!!.data[0].sta
                            }*/
                            button_offline.visibility = View.GONE
                            card_online.visibility = View.VISIBLE
                            moon_offline.visibility = View.GONE
                        }
                    } else {
                        //  (activity as HomeActivity).offSwitchButton()
                        if (SharedPreferenceUtils.getInstance(requireContext())
                                ?.getStringValue(ConstantUtils.DRIVER_STATUS, "1").equals("2")
                        ) {
                            ToastUtil.toast_Long(activity, response.body()!!.msg)
                        }


                    }

                } else {
                    // (activity as HomeActivity).offSwitchButton()
                }


            }

            override fun onFailure(call: Call<DriverTripResponse>, t: Throwable) {
                rlLoader.visibility = View.GONE
                ToastUtil.toast_Long(activity, t.toString())
                // (activity as HomeActivity).offSwitchButton()

            }


        })

    }

    private fun acceptTrip() {
        rlLoader.visibility = View.VISIBLE
        var hashMap: HashMap<String, String> = HashMap<String, String>()
        hashMap.put("driver_id", StringUtil.getUserId(requireContext()))
        hashMap.put("booking_id", booking_id)

        val apiCall = ApiInterface.create().acceptTrip(hashMap)
        apiCall.enqueue(object : Callback<AcceptTripResponse> {
            override fun onResponse(
                call: Call<AcceptTripResponse>,
                response: Response<AcceptTripResponse>
            ) {
                rlLoader.visibility = View.GONE
                if (response.isSuccessful) {
                    if (response.body()!!.success) {
                        card_online.visibility = View.GONE
                        rlStartRide.visibility = View.VISIBLE
                        ToastUtil.toast_Long(activity, response.body()!!.msg)
                    } else {

                        ToastUtil.toast_Long(activity, response.body()!!.msg)

                    }

                } else {
                    ToastUtil.toast_Long(activity, resources.getString(R.string.servererror))
                }


            }

            override fun onFailure(call: Call<AcceptTripResponse>, t: Throwable) {
                rlLoader.visibility = View.GONE
                ToastUtil.toast_Long(activity, t.toString())


            }


        })

    }

    private fun rejectTrip() {
        rlLoader.visibility = View.VISIBLE
        var hashMap: HashMap<String, String> = HashMap<String, String>()
        hashMap.put("driver_id", StringUtil.getUserId(requireContext()))
        hashMap.put("booking_id", booking_id)

        val apiCall = ApiInterface.create().rejectTrip(hashMap)
        apiCall.enqueue(object : Callback<AcceptTripResponse> {
            override fun onResponse(
                call: Call<AcceptTripResponse>,
                response: Response<AcceptTripResponse>
            ) {
                rlLoader.visibility = View.GONE
                if (response.isSuccessful) {
                    if (response.body()!!.success) {
                        card_online.visibility = View.GONE
                        ToastUtil.toast_Long(activity, response.body()!!.msg)
                    } else {

                        ToastUtil.toast_Long(activity, response.body()!!.msg)

                    }

                } else {
                    ToastUtil.toast_Long(activity, resources.getString(R.string.servererror))
                }


            }

            override fun onFailure(call: Call<AcceptTripResponse>, t: Throwable) {
                rlLoader.visibility = View.GONE
                ToastUtil.toast_Long(activity, t.toString())


            }


        })

    }



    private fun updateDriverLocation() {
        rlLoader.visibility = View.VISIBLE
        var hashMap: HashMap<String, String> = HashMap<String, String>()
        hashMap.put("driver_id", StringUtil.getUserId(requireContext()))
        SharedPreferenceUtils.getInstance(requireContext())!!
            .getStringValue(ConstantUtils.CURENT_LAT, "")
            ?.let { hashMap.put("latitude", it) }
        SharedPreferenceUtils.getInstance(requireContext())!!
            .getStringValue(ConstantUtils.CURENT_LNG, "")
            ?.let { hashMap.put("longitude", it) }
        SharedPreferenceUtils.getInstance(requireContext())!!
            .getStringValue(ConstantUtils.LOCATION, "")
            ?.let { hashMap.put("location_name", it) }
        SharedPreferenceUtils.getInstance(requireContext())!!
            .getStringValue(ConstantUtils.DRIVER_STATUS, "1")
            ?.let { hashMap.put("status", it) }

        val apiCall = ApiInterface.create().updateLocation(hashMap)
        apiCall.enqueue(object : Callback<LocationUpdateResponse> {
            override fun onResponse(
                call: Call<LocationUpdateResponse>,
                response: Response<LocationUpdateResponse>
            ) {
                rlLoader.visibility = View.GONE
                if (response.isSuccessful) {
                    if (response.body()!!.success) {
                        if (SharedPreferenceUtils.getInstance(requireContext())
                                ?.getStringValue(ConstantUtils.DRIVER_STATUS, "1").equals("2")
                        ) {
                            moon_offline.visibility = View.GONE
                            getDriverTripInfo()
                        } else {
                            moon_offline.visibility = View.VISIBLE
                            button_offline.visibility = View.VISIBLE
                            card_online.visibility = View.GONE
                        }

                        //  ToastUtil.toast_Long(activity, response.body()!!.msg)
                    } else {

                        ToastUtil.toast_Long(activity, response.body()!!.msg)

                    }

                } else {
                    ToastUtil.toast_Long(activity, resources.getString(R.string.servererror))
                }


            }

            override fun onFailure(call: Call<LocationUpdateResponse>, t: Throwable) {
                rlLoader.visibility = View.GONE
                ToastUtil.toast_Long(activity, t.toString())


            }


        })

    }

}

package com.app.whirl.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.app.whirl.R
import kotlinx.android.synthetic.main.activity_register_three.*
import kotlinx.android.synthetic.main.activity_select_vehicle_type.*

class SelectVehicleTypeActivity : AppCompatActivity() {
    var email: String = ""

    var fName: String = ""

    var lName: String = ""

    var earntype = "car owner"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_vehicle_type)
        cardCarOwner.setOnClickListener(clickListener)
        cardPartner.setOnClickListener(clickListener)
        cardWhirlMoto.setOnClickListener(clickListener)
        getData()
        cardWhirlMoto.setBackgroundResource(R.drawable.bg_rounded_vehical)
        cardCarOwner.setBackgroundResource(R.drawable.bg_rounded_vehical_red)
        cardPartner.setBackgroundResource(R.drawable.bg_rounded_vehical)

        changeCarOwnerView()
    }


    private fun getData() {
        fName = intent.getStringExtra("fname").toString()
        lName = intent.getStringExtra("lname").toString()
        email = intent.getStringExtra("email").toString()
    }


    private val clickListener: View.OnClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.cardCarOwner -> carOwner()
            R.id.cardPartner -> carPartner()
            R.id.cardWhirlMoto -> carWhirlMoto()
        }
    }

    private fun carWhirlMoto() {
        //TODO("Not yet implemented")
        cardWhirlMoto.setBackgroundResource(R.drawable.bg_rounded_vehical_red)
        cardCarOwner.setBackgroundResource(R.drawable.bg_rounded_vehical)
        cardPartner.setBackgroundResource(R.drawable.bg_rounded_vehical)
        changeMotoView()

        earntype = "whirl moto"
    }

    private fun changeMotoView() {
        tvCarOwner.setTextColor(resources.getColor(R.color.textGrey))
        tvCarOwnerText1.setTextColor(resources.getColor(R.color.textGrey))
        tvCarOwnerText2.setTextColor(resources.getColor(R.color.textGrey))

        tvWhirlMotoText1.setTextColor(resources.getColor(R.color.colorWhite))
        tvWhirlMotoText2.setTextColor(resources.getColor(R.color.colorWhite))

        tvPartnerdriverText1.setTextColor(resources.getColor(R.color.textGrey))
        tvPartnerdriverText2.setTextColor(resources.getColor(R.color.textGrey))


    }

    private fun carPartner() {
        //TODO("Not yet implemented")
        cardWhirlMoto.setBackgroundResource(R.drawable.bg_rounded_vehical)
        cardCarOwner.setBackgroundResource(R.drawable.bg_rounded_vehical)
        cardPartner.setBackgroundResource(R.drawable.bg_rounded_vehical_red)

        changeCarView()

        earntype = "partner driver"
    }

    private fun changeCarView() {

        tvCarOwner.setTextColor(resources.getColor(R.color.textGrey))
        tvCarOwnerText1.setTextColor(resources.getColor(R.color.textGrey))
        tvCarOwnerText2.setTextColor(resources.getColor(R.color.textGrey))

        tvWhirlMotoText1.setTextColor(resources.getColor(R.color.textGrey))
        tvWhirlMotoText2.setTextColor(resources.getColor(R.color.textGrey))

        tvPartnerdriverText1.setTextColor(resources.getColor(R.color.colorWhite))
        tvPartnerdriverText2.setTextColor(resources.getColor(R.color.colorWhite))

    }

    private fun carOwner() {
        //TODO("Not yet implemented")
        cardWhirlMoto.setBackgroundResource(R.drawable.bg_rounded_vehical)
        cardCarOwner.setBackgroundResource(R.drawable.bg_rounded_vehical_red)
        cardPartner.setBackgroundResource(R.drawable.bg_rounded_vehical)

        changeCarOwnerView()
        earntype = "car owner"
    }

    private fun changeCarOwnerView() {

        tvCarOwner.setTextColor(resources.getColor(R.color.colorWhite))
        tvCarOwnerText1.setTextColor(resources.getColor(R.color.colorWhite))
        tvCarOwnerText2.setTextColor(resources.getColor(R.color.colorWhite))

        tvWhirlMotoText1.setTextColor(resources.getColor(R.color.textGrey))
        tvWhirlMotoText2.setTextColor(resources.getColor(R.color.textGrey))

        tvPartnerdriverText1.setTextColor(resources.getColor(R.color.textGrey))
        tvPartnerdriverText2.setTextColor(resources.getColor(R.color.textGrey))

    }

    fun onClickBack(view: View) {

        onBackPressed()
    }

    fun onClickContinue(view: View) {

        val intent = Intent(this, UploadDocumentActivity::class.java)
        intent.putExtra("fname", fName)
        intent.putExtra("lname", lName)
        intent.putExtra("email", email)
        intent.putExtra("vehicleType", earntype)
        startActivity(intent)

    }
}
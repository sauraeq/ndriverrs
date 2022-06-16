package com.whirl.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.app.whirl.HomeActivity
import com.app.whirl.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject


class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val gson = Gson()
        val json = gson.toJson(remoteMessage)


        Log.d("firebasemessage", "onMessageReceived: ${json}")
          shownotification(remoteMessage)

        // val emp: JSONObject = JSONObject(JSON_STRING).getJSONObject("employee")

      /*  val jsonObj: JSONObject = JSONObject(json)
        var bundle = jsonObj.getJSONObject("bundle")
        var mMap = bundle.getJSONObject("mMap")
        var deriverDetail = mMap.get("deriverDetail")
        val jsonArray: JSONArray = JSONArray(deriverDetail.toString())
        val c: JSONObject = jsonArray.getJSONObject(0)
        var fname = c.getString("fname")
        var phone_no = c.getString("phone_no")
        var amount = c.getString("amount")
        var profile_photo = c.getString("profile_photo")
        var latitude = c.getString("latitude")
        var drop_address = c.getString("drop_address")
        var rating = c.getString("rating")
        var pickup_address = c.getString("pickup_address")
        var lname = c.getString("lname")
        var device_tokan = c.getString("device_tokan")
        var vehicle_number = c.getString("vehicle_number")
        var id = c.getString("id")
        var email = c.getString("email")
        var vehicle_name = c.getString("vehicle_name")

        var longitude = c.getString("longitude")*/




    }


    private fun shownotification(remoteMessage: RemoteMessage) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val channelId = "Default"
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(remoteMessage.notification!!.title)
            .setContentText(remoteMessage.notification!!.body).setAutoCancel(true)
            .setContentIntent(pendingIntent)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }
        manager.notify(0, builder.build())
    }
}

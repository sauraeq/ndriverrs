package com.app.whirl.utils

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestListener

import com.bumptech.glide.request.target.Target
import java.lang.Exception
import javax.sql.DataSource

/**
 *
 */
object Utils {

    //to get the device id of current device
    fun getDeviceId(context: Context): String {
        // LogUtils.error("Unique generated id  : " + android_id);
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    /**
     * to hide the Keyboard
     *
     * @return
     */
    fun hideSoftKeyboard(activity: Activity, view: View?) {
        var view = view
        try {
            if (view == null) {
                view = activity.getCurrentFocus()
            }
            val `in` = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            `in`.hideSoftInputFromWindow(view!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    /**
     * will toast message
     *
     * @param context
     * @param msg
     */
    fun showToastMessage(context: Context?, msg: String?) {
        val toast: Toast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
        toast.show()
    }

    /**
     * show message In Toast.
     *
     * @param context
     * @param string
     */
    fun showToast(context: Context?, string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }



    /**
     * Set the gradient color and stroke color
     *
     * @param view
     * @param backGroundColor
     * @param strokeColor
     */
    fun setGradientColorAndStrokeToView(
        view: View,
        backGroundColor: Int,
        strokeColor: Int,
        strokeWidth: Int
    ) {
        val drawable: GradientDrawable = view.background as GradientDrawable
        drawable.setColor(backGroundColor)
        drawable.setStroke(strokeWidth, strokeColor)
    }



}
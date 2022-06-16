package com.example.ranierilavastone.Utils

import android.content.Context
import android.util.Patterns
import com.metaled.Utils.ConstantUtils
import com.metaled.Utils.SharedPreferenceUtils

class StringUtil {
    companion object {
        fun capString(str: String): String {
            try {
                return str[0].uppercaseChar() + str.substring(1).lowercase()
            } catch (e: Exception) {
                return ""
            }


        }

        fun getUserId(context: Context): String {
            var userId = SharedPreferenceUtils.getInstance(context)
                ?.getStringValue(ConstantUtils.USER_ID, "")
            return userId!!

        }
        fun isEmailValid(email: String?): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}
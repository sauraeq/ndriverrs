package com.app.whirl.pref

import android.content.Context
import android.content.SharedPreferences
import com.app.whirl.model.UserDTO
import kotlin.jvm.Volatile
import com.app.whirl.pref.AppPref
import com.google.gson.Gson

/**
 *
 */
/**
 * Make Class shared Preference to set and get the response.
 */
class AppPref private constructor(context: Context) {
    private val preferences: SharedPreferences
    private val KEY_AUTO_LOGIN = "autoLogin"
    private val KEY_PERMISSION = "permission"

    //    /***
    var isLogin: Boolean
        get() = preferences.getBoolean(KEY_AUTO_LOGIN, false)
        set(value) {
            preferences.edit().putBoolean(KEY_AUTO_LOGIN, value).apply()
        }

    fun   getPersonDetails():UserDTO{

       var  gson: Gson  =  Gson()
       // gson.fromJson<UserDTO>(preferences.getString(USER_DATA, null)

        return gson.fromJson(preferences.getString(USER_DATA, null),UserDTO::class.java)
      //  UserDTO p = gson.fromJson(preferences.getString(USER_DATA, null), UserDTO.class);
    }


    fun savePersonDetails(userDTO: UserDTO){
        var  gson: Gson  =  Gson()
        preferences.edit().putString(USER_DATA, gson.toJson(userDTO)).apply();
    }


    //     * getter for provider
    //     * @return instance of user
    //     */
    //    public UserDTO getPersonDetail() {
    //        Gson gson = new Gson();
    //        UserDTO p = gson.fromJson(preferences.getString(USER_DATA, null), UserDTO.class);
    //        if (p == null)
    //            p = new UserDTO();
    //        return p;
    //    }
    //
    //    /***
    //     * to add user instance into preferences
    //     * @param user instance of provider
    //     */
    //    public void savePersonDetail(UserDTO user) {
    //        Gson gson = new Gson();
    //        preferences.edit().putString(USER_DATA, gson.toJson(user)).apply();
    //
    //    }
    //    public void userLogout() {
    //        savePersonDetail(new UserDTO());
    //        setLogin(false);
    //    }
    companion object {
        private const val PREF_NAME = "myPreferences"
        private const val USER_DATA = "user_data"
        private const val APP_CONFIG = "app_config"

        @Volatile
        private var appPreferences: AppPref? = null
        fun getInstance(context: Context): AppPref? {
            if (appPreferences == null) {
                synchronized(AppPref::class.java) {
                    if (appPreferences == null) appPreferences = AppPref(context)
                }
            }
            return appPreferences
        }
    }

    init {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
}
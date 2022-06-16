package com.app.whirl.network
import com.app.whirl.model.Login
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


interface ApiInterface {

    companion object {


        var BASE_URL = "http://dev.equalinfotech.in/shareride/api/"

        fun create() : WhirlService {

            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL).client(client)
                .build()
            return retrofit.create(WhirlService::class.java)

        }
    }

}
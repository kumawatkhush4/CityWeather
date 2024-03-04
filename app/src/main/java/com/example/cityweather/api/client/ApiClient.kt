package com.tunetap.api.client

import com.example.cityweather.interfaces.ApiInterface
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val baseURL = "https://api.openweathermap.org/data/2.5/"

    /* private var client: OkHttpClient? = null*/

    val getClient: ApiInterface
        get(){
            val gson  = GsonBuilder().setLenient().create()
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder().connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(120, TimeUnit.MINUTES).readTimeout(3, TimeUnit.MINUTES)
                .addInterceptor(interceptor).build()

            /*val client = OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(120,TimeUnit.MINUTES)
                .readTimeout(3,TimeUnit.MINUTES)
                .build()*/

            val retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            return retrofit.create(ApiInterface::class.java)
        }

}

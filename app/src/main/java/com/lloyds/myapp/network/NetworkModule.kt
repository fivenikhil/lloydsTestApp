package com.lloyds.myapp.network

import com.lloyds.myapp.BuildConfig
import com.lloyds.myapp.constants.AppConstant.Companion.MEAL_CATEGORY_API
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {

    private val retrofitClient: Retrofit.Builder by lazy {

        val levelType: HttpLoggingInterceptor.Level =
            if (BuildConfig.BUILD_TYPE.contentEquals("debug")) {
                HttpLoggingInterceptor.Level.BODY
                HttpLoggingInterceptor.Level.HEADERS
            } else HttpLoggingInterceptor.Level.NONE

        val logging = HttpLoggingInterceptor()
        logging.apply { logging.level = levelType }

        val okhttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request()
                    val authenticatedRequest = request.newBuilder()
                        .header("Content-Type", "application/json")
                        .build()
                    return chain.proceed(authenticatedRequest)
                }
            })

        Retrofit.Builder()
            .baseUrl(MEAL_CATEGORY_API)
            .client(okhttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())

    }

    val apiInterface: APIService by lazy {
        retrofitClient
            .build()
            .create(APIService::class.java)
    }
}


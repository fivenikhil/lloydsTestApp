package com.lloyds.myapp.network

import android.content.Context
import com.lloyds.myapp.constants.AppConstant.Companion.MEAL_CATEGORY_API
import com.lloyds.myapp.utils.ConnectivityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun theRetrofitInstance(): APIService {
        val okhttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    // Implement your interceptor logic here
                    val request = chain.request()
                    val authenticatedRequest = request.newBuilder()
                        .header("Content-Type", "application/json")
                        .build()
                    return chain.proceed(authenticatedRequest)
                }
            })

        val API: APIService by lazy {
            Retrofit.Builder()
                .baseUrl(MEAL_CATEGORY_API)
                .client(okhttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIService::class.java)
        }
        return API
    }

}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideConnectivityRepository(@ApplicationContext context: Context): ConnectivityRepository {
        return ConnectivityRepository(context)
    }
}


package com.kyawhtut.pos.data.api.network

import android.content.Context
import com.kyawhtut.pos.BuildConfig
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author kyawhtut
 * @date 25/05/2020
 */
class ApiRetrofit {

    private fun provideRetrofit(
        context: Context,
        baseURL: String,
        internetConnectivityInterceptor: InternetConnectivityInterceptor
    ): Retrofit {
        val builder = UnsafeOkHttpClient.getUnsafeOkHttpClient().apply {
            connectTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            addInterceptor(internetConnectivityInterceptor)
        }

        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.networkInterceptors().add(httpLoggingInterceptor)
            builder.addInterceptor(ChuckInterceptor(context))
        }

        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(builder.build())
            .build()
    }

    fun provideApi(
        context: Context,
        baseURL: String,
        internetConnectivityInterceptor: InternetConnectivityInterceptor
    ): API =
        provideRetrofit(context, baseURL, internetConnectivityInterceptor).create(API::class.java)
}

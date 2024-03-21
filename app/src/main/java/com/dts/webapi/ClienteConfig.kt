package com.dts.webapi

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ClienteConfig(cont: Context?, private var apiurl: String) {
    fun <S> CrearServicio(claseServicio: Class<S>?, aurl: String): S {
        apiurl = aurl
        cliente
        return retrofit!!.create(claseServicio)
    }

    val cliente: Unit
        get() {
            val cliente = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .addInterceptor { chain: Interceptor.Chain ->
                    val request = chain.request()
                    val newChain = chain.withReadTimeout(10, TimeUnit.SECONDS)
                    newChain.proceed(request)
                }
                .build()
            val gson = GsonBuilder()
                .setLenient()
                .create()
            retrofit = Retrofit.Builder()
                .baseUrl(apiurl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(cliente)
                .build()
        }

    companion object {
        private var retrofit: Retrofit? = null
    }
}
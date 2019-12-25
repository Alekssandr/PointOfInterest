package com.szczecin.pointofinterest.data.network

import com.szczecin.pointofinterest.data.network.RetrofitProvider.provideRetrofit
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class NetworkProvider(private val url: String) {

    fun provideCommonApiRetrofit(): Retrofit = provideRetrofit(
        url,
        provideOkHttpClient()
    )

    private fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .build()
}
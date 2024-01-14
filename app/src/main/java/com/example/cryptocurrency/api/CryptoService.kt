package com.example.cryptocurrency.api

import com.example.cryptocurrency.models.ListResponse
import com.example.cryptocurrency.models.LiveResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoService {

    @GET("live")
    suspend fun getExchangeRates(@Query("access_key") apiKey: String): Response<LiveResponse>

    @GET("list")
    suspend fun getCryptoList(@Query("access_key") apiKey: String): Response<ListResponse>

}
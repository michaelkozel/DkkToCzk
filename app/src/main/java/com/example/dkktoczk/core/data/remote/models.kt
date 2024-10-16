package com.example.dkktoczk.core.data.remote

import retrofit2.http.GET

data class ExchangeRate(
    val czk: Double,
    val dkk: Double,
    val lastUpdated: Long
)

interface  ExchangeRateApi {
    @GET("latest?base=CZK&symbols=DKK")
    suspend fun getLatestRate(): ExchangeRateResponse
}

data class ExchangeRateResponse(
    val rates: Map<String, Double>,
    val base: String,
    val date: String
)
package com.example.dkktoczk.core.data.repository

import com.example.dkktoczk.core.data.local.ExchangeRateDao
import com.example.dkktoczk.core.data.local.ExchangeRateEntity
import com.example.dkktoczk.core.data.remote.ExchangeRate
import com.example.dkktoczk.core.data.remote.ExchangeRateApi
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val api: ExchangeRateApi,
    private val dao: ExchangeRateDao
) {
    suspend fun getLatestRate(): ExchangeRate {
        val localRate = dao.getExchangeRate()
        return if (localRate != null && isRateFresh(localRate.lastUpdated)) {
            ExchangeRate(localRate.czk, localRate.dkk, localRate.lastUpdated)
        } else {
            val remoteRate = api.getLatestRate()
            val newRate = ExchangeRate(1.0, remoteRate.rates["DKK"] ?: 0.0, System.currentTimeMillis())
            dao.insertExchangeRate(ExchangeRateEntity(czk = newRate.czk, dkk = newRate.dkk, lastUpdated = newRate.lastUpdated))
            newRate
        }
    }

    private fun isRateFresh(lastUpdated: Long): Boolean {
        val oneDayInMillis = 24 * 60 * 60 * 1000
        return System.currentTimeMillis() - lastUpdated < oneDayInMillis
    }
}

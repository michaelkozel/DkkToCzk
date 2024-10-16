package com.example.dkktoczk.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.dkktoczk.core.data.repository.CurrencyRepository
import javax.inject.Inject

class ExchangeRateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    @Inject
    lateinit var repository: CurrencyRepository

    override suspend fun doWork(): Result {
        return try {
            repository.getLatestRate()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
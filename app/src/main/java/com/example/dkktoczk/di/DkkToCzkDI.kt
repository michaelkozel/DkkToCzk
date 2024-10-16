package com.example.dkktoczk.di

import android.content.Context
import androidx.room.Room
import com.example.dkktoczk.core.data.local.AppDatabase
import com.example.dkktoczk.core.data.remote.ExchangeRateApi
import com.example.dkktoczk.core.data.repository.CurrencyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCurrencyApi(): ExchangeRateApi {
        return Retrofit.Builder()
            .baseUrl("https://api.exchangerate-api.com/v4/latest/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExchangeRateApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCurrencyDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "currency_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCurrencyRepository(
        api: ExchangeRateApi,
        db: AppDatabase
    ): CurrencyRepository {
        return CurrencyRepository(api, db.exchangeRateDao())
    }
}
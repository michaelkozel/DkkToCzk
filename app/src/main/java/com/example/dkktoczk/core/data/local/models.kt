package com.example.dkktoczk.core.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import dagger.Provides
import javax.inject.Singleton

@Entity(tableName = "exchange_rates")
data class ExchangeRateEntity(
    @PrimaryKey val id: Int = 1,
    val czk: Double,
    val dkk: Double,
    val lastUpdated: Long
)

@Dao
interface ExchangeRateDao {
    @Query("SELECT * FROM exchange_rates WHERE id = 1")
    suspend fun getExchangeRate(): ExchangeRateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExchangeRate(rate: ExchangeRateEntity)
}

@Database(entities = [ExchangeRateEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exchangeRateDao(): ExchangeRateDao
}
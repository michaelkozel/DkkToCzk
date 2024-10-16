package com.example.dkktoczk.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dkktoczk.core.data.remote.ExchangeRate
import com.example.dkktoczk.core.data.repository.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val repository: CurrencyRepository
) : ViewModel() {
    private val _exchangeRate = MutableStateFlow<ExchangeRate?>(null)
    val exchangeRate: StateFlow<ExchangeRate?> = _exchangeRate.asStateFlow()

    private val _conversionResult = MutableStateFlow<Double?>(null)
    val conversionResult: StateFlow<Double?> = _conversionResult.asStateFlow()

    init {
        viewModelScope.launch {
            _exchangeRate.value = repository.getLatestRate()
        }
    }

    fun convertCurrency(amount: Double, fromCzk: Boolean) {
        val rate = _exchangeRate.value ?: return
        _conversionResult.value = if (fromCzk) {
            amount * rate.dkk
        } else {
            amount / rate.dkk
        }
    }
}
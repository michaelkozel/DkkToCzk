package com.example.dkktoczk.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dkktoczk.ui.theme.DkkToCzkTheme
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("DefaultLocale")
@Composable
fun CurrencyConverterScreen(viewModel: CurrencyViewModel = hiltViewModel()) {
    val exchangeRate by viewModel.exchangeRate.collectAsState()
    val conversionResult by viewModel.conversionResult.collectAsState()
    var amount by remember { mutableStateOf("") }
    var isCzkToDkk by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Currency Converter",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = if (isCzkToDkk) "CZK to DKK" else "DKK to CZK")
            Switch(
                checked = isCzkToDkk,
                onCheckedChange = { isCzkToDkk = it }
            )
        }

        Button(
            onClick = {
                amount.toDoubleOrNull()?.let { value ->
                    viewModel.convertCurrency(value, isCzkToDkk)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Convert")
        }

        Spacer(modifier = Modifier.height(16.dp))

        conversionResult?.let { result ->
            Text(
                text = "Result: ${String.format("%.2f", result)} ${if (isCzkToDkk) "DKK" else "CZK"}",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        exchangeRate?.let { rate ->
            Text(
                text = "1 DKK = ${String.format("%.3f", 1/rate.dkk)} DKK",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "Last updated: ${SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(
                    Date(rate.lastUpdated)
                )}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencyConverterScreenPreview() {
    DkkToCzkTheme {
        CurrencyConverterScreen()
    }
}
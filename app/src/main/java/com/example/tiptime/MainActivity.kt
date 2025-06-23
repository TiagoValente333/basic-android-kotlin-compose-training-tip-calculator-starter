/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.tiptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiptime.ui.theme.TipTimeTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    FuelCostLayout()
                }
            }
        }
    }
}

@Composable
fun FuelCostLayout() {
    var fuelPriceInput by remember { mutableStateOf("") }
    var fuelConsumptionInput by remember { mutableStateOf("") }
    var distanceInput by remember { mutableStateOf("") }

    val fuelPrice = fuelPriceInput.toDoubleOrNull() ?: 0.0
    val fuelConsumption = fuelConsumptionInput.toDoubleOrNull() ?: 0.0
    val distance = distanceInput.toDoubleOrNull() ?: 0.0

    val totalCost = calculateFuelCost(fuelPrice, fuelConsumption, distance)

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Calculadora de Custo de Combustível",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )

        // Preço do combustível
        EditNumberField(
            value = fuelPriceInput,
            onValueChanged = { fuelPriceInput = it },
            label = "Preço do combustível (por litro)",
            modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth()
        )

        // Consumo do veículo
        EditNumberField(
            value = fuelConsumptionInput,
            onValueChanged = { fuelConsumptionInput = it },
            label = "Consumo do veículo (l/100km)",
            modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth()
        )

        // Distância a percorrer
        EditNumberField(
            value = distanceInput,
            onValueChanged = { distanceInput = it },
            label = "Distância a percorrer (km)",
            modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth()
        )

        Text(
            text = "Custo total: $totalCost",
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
fun EditNumberField(
    value: String,
    onValueChanged: (String) -> Unit,
    label: String,
    modifier: Modifier
) {
    TextField(
        value = value,
        singleLine = true,
        modifier = modifier,
        onValueChange = onValueChanged,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

/**
 * Calculates the total fuel cost based on:
 * @param fuelPrice price per liter of fuel
 * @param fuelConsumption vehicle consumption in liters per 100km
 * @param distance distance to travel in kilometers
 */
private fun calculateFuelCost(fuelPrice: Double, fuelConsumption: Double, distance: Double): String {
    if (fuelConsumption == 0.0 || distance == 0.0) {
        return NumberFormat.getCurrencyInstance().format(0.0)
    }
    val litersNeeded = (fuelConsumption / 100) * distance
    val totalCost = litersNeeded * fuelPrice
    return NumberFormat.getCurrencyInstance().format(totalCost)
}

@Preview(showBackground = true)
@Composable
fun FuelCostLayoutPreview() {
    TipTimeTheme {
        FuelCostLayout()
    }
}
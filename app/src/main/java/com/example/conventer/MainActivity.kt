package com.example.conventer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.conventer.ui.theme.*
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConventerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ConverterScreen()
                }
            }
        }
    }
}

val exchangeRates = mapOf(
    "USD - US Dollar" to 16789.0,
    "EUR - Euro" to 19071.0,
    "JPY - Japanese Yen" to 117.1,
    "GBP - British Pound" to 22114.7,
    "AUD - Australian Dollar" to 10640.0,
    "CAD - Canadian Dollar" to 12080.0,
    "SGD - Singapore Dollar" to 12750.0,
    "MYR - Malaysian Ringgit" to 3812.0,
    "THB - Thai Baht" to 500.3,
    "CNY - Chinese Yuan" to 2294.0,
    "CHF - Swiss Franc" to 20587.2,
    "PHP - Philippine Peso" to 296.4,
    "HKD - Hong Kong Dollar" to 2168.8,
    "KRW - South Korean Won" to 12.5,
    "INR - Indian Rupee" to 200.9,
    "BRL - Brazilian Real" to 3200.0,
    "MXN - Mexican Peso" to 900.5
)

@Composable
fun ConverterScreen() {
    var idrAmount by remember { mutableStateOf("") }
    var selectedCurrency by remember { mutableStateOf(exchangeRates.keys.first()) }
    var result by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var showInfo by remember { mutableStateOf(false) }
    var isReversed by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        // Header with gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                )
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "💰 Currency Converter",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            )
        }

        Spacer(Modifier.height(16.dp))

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // IDR Input Card
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "INDONESIAN RUPIAH",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                                OutlinedTextField(
                                value = idrAmount,
                        onValueChange = { if(it.matches(Regex("^\\d*\\.?\\d*\$"))) idrAmount = it },
                        label = { Text("Enter amount in ${if (isReversed) selectedCurrency.split(" - ")[0] else "IDR"}") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Text(
                                "",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(end = 4.dp)
                            )
                        }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Currency Selector Card
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "TARGET CURRENCY",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = selectedCurrency,
                            onValueChange = {},
                            label = { Text("Select currency") },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { expanded = true }) {
                                    Icon(
                                        Icons.Default.ArrowDropDown,
                                        null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                focusedLabelColor = MaterialTheme.colorScheme.primary,
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            exchangeRates.keys.forEach { currency ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            currency,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    },
                                    onClick = {
                                        selectedCurrency = currency
                                        expanded = false
                                        result = null
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            TextButton(
                onClick = {
                    isReversed = !isReversed
                    result = null // reset hasil
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("🔁 Reverse to ${if (isReversed) "IDR → Currency" else "Currency → IDR"}")
            }

            // Convert Button
            Button(
                onClick = {
                    when {
                        idrAmount.isBlank() -> result = "Please enter amount"
                        idrAmount.toDoubleOrNull() == null -> result = "Invalid amount"
                        else -> exchangeRates[selectedCurrency]?.let { rate ->
                            val input = idrAmount.toDouble()
                            val output = if (isReversed) input * rate else input / rate
                            val from = if (isReversed) selectedCurrency.split(" - ")[0] else "IDR"
                            val to = if (isReversed) "IDR" else selectedCurrency.split(" - ")[0]
                            result = "%,.2f %s = %,.2f %s".format(Locale.US, input, from, output, to)
                        } ?: run { result = "Rate not found" }
                    }
                },

                    colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    "CONVERT NOW",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                )
            }

            Spacer(Modifier.height(32.dp))

            // Result Display
            result?.let {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(8.dp),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "CONVERSION RESULT",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                            )
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            it,
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            ),
                            textAlign = TextAlign.Center
                        )

                        // Add conversion rate info
                        Spacer(Modifier.height(16.dp))

                        exchangeRates[selectedCurrency]?.let { rate ->
                            Text(
                                "1 ${selectedCurrency.split(" - ")[0]} = ${String.format(Locale.US,"%,.2f", rate)} IDR",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                                )
                            )
                        }
                    }
                }
            }
        }

        // Footer
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                thickness = 1.dp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            TextButton(
                onClick = { showInfo = !showInfo },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            ) {
                Text(
                    "Nabilah Atika Rahma • NRP: 5025221005",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (showInfo) {
                Text(
                    "Currency data updated: April 2024",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
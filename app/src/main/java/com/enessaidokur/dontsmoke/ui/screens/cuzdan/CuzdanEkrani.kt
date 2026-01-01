package com.enessaidokur.dontsmoke.ui.screens.cuzdan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enessaidokur.dontsmoke.R
import com.enessaidokur.dontsmoke.ui.components.acikGriArkaPlan
import com.enessaidokur.dontsmoke.ui.components.acikYesil
import com.enessaidokur.dontsmoke.ui.components.anaYesil
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuzdanEkrani(viewModel: CuzdanViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    val locale = Locale("tr", "TR")
    val currencyFormatter = NumberFormat.getCurrencyInstance(locale)
    val numberFormatter = NumberFormat.getNumberInstance(locale).apply {
        maximumFractionDigits = 6
        minimumFractionDigits = 2
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Cüzdanım",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = acikYesil)
            )
        },
        containerColor = acikGriArkaPlan
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                TasarrufKarti(
                    toplamDegerFormatli = uiState.toplamCuzdanDegeriFormatli,
                    isLoading = uiState.isFiyatlarLoading
                )
            }
            if (uiState.varliklar.isNotEmpty()) {
                item {
                    Text(
                        "Varlıklarım",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp),
                        color = anaYesil
                    )
                }
            }
            if (uiState.hataMesaji != null && !uiState.isFiyatlarLoading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.hataMesaji!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
            items(uiState.varliklar) { varlik ->
                VarlikKarti(
                    varlik = varlik,
                    currencyFormatter = currencyFormatter,
                    numberFormatter = numberFormatter
                )
            }
        }
    }
}

@Composable
fun TasarrufKarti(toplamDegerFormatli: String, isLoading: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = anaYesil)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Toplam Varlıklarım",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(36.dp), color = Color.White)
            } else {
                Text(
                    text = toplamDegerFormatli,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun VarlikKarti(
    varlik: CuzdanVarlik,
    currencyFormatter: NumberFormat,
    numberFormatter: NumberFormat
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = varlik.ikonResId),
                contentDescription = varlik.ad,
                modifier = Modifier.size(40.dp),
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    varlik.ad,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Miktar: ${numberFormatter.format(varlik.miktar)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Text(
                    text = "Anlık Fiyat: ${currencyFormatter.format(varlik.anlikFiyat)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }
            Text(
                text = currencyFormatter.format(varlik.toplamDeger),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }
    }
}
package com.enessaidokur.dontsmoke.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enessaidokur.dontsmoke.R
import com.enessaidokur.dontsmoke.ui.components.acikGriArkaPlan
import com.enessaidokur.dontsmoke.ui.components.acikYesil
import com.enessaidokur.dontsmoke.ui.components.anaYesil
import com.enessaidokur.dontsmoke.ui.components.koyuMetin
import com.enessaidokur.dontsmoke.ui.viewmodel.CuzdanViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun YatirimEkrani(cuzdanViewModel: CuzdanViewModel) {
    val cuzdanState by cuzdanViewModel.uiState.collectAsState()

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = acikYesil, darkIcons = false)
        systemUiController.setNavigationBarColor(color = Color.Transparent, darkIcons = true)
    }

    Scaffold(
        topBar = { KalpRitmiAppBar() },
        containerColor = acikGriArkaPlan
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = anaYesil),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Kullanılabilir Bakiye", fontSize = 16.sp, color = Color.White.copy(alpha = 0.8f))
                    Text("₺%.2f".format(cuzdanState.bakiye), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            YatirimAraciKarti(
                isim = "Altın",
                guncelFiyat = 5042.0,
                ikonResId = R.drawable.altin,
                mevcutBakiye = cuzdanState.bakiye,
                butonAl = { harcananTutar, alinanMiktar ->
                    cuzdanViewModel.satinAl("Altın", alinanMiktar, harcananTutar, "gr", 5042.0, R.drawable.altin)
                }
            )
            YatirimAraciKarti(
                isim = "Gümüş",
                guncelFiyat = 66.0,
                ikonResId = R.drawable.gumus,
                mevcutBakiye = cuzdanState.bakiye,
                butonAl = { harcananTutar, alinanMiktar ->
                    cuzdanViewModel.satinAl("Gümüş", alinanMiktar, harcananTutar, "gr", 66.0, R.drawable.gumus)
                }
            )
            YatirimAraciKarti(
                birim = "$",
                isim = "Dolar",
                guncelFiyat = 41.84,
                ikonResId = R.drawable.dolar,
                mevcutBakiye = cuzdanState.bakiye,
                butonAl = { harcananTutar, alinanMiktar ->
                    cuzdanViewModel.satinAl("Dolar", alinanMiktar, harcananTutar, "$", 41.84, R.drawable.dolar)
                }
            )
            YatirimAraciKarti(
                birim = "€",
                isim = "Euro",
                guncelFiyat = 48.45,
                ikonResId = R.drawable.euro,
                mevcutBakiye = cuzdanState.bakiye,
                butonAl = { harcananTutar, alinanMiktar ->
                    cuzdanViewModel.satinAl("Euro", alinanMiktar, harcananTutar, "€", 48.45, R.drawable.euro)
                }
            )
            YatirimAraciKarti(
                birim = "BTC",
                isim = "Bitcoin",
                guncelFiyat = 4524920.73,
                ikonResId = R.drawable.bitcoin,
                mevcutBakiye = cuzdanState.bakiye,
                butonAl = { harcananTutar, alinanMiktar ->
                    cuzdanViewModel.satinAl("Bitcoin", alinanMiktar, harcananTutar, "BTC", 4524920.73, R.drawable.bitcoin)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YatirimAraciKarti(
    isim: String,
    guncelFiyat: Double,
    @DrawableRes ikonResId: Int,
    mevcutBakiye: Double,
    butonAl: (Double, Double) -> Unit, // (TL, Miktar)
    birim: String = "gr"
) {
    var girilenTutar by remember { mutableStateOf("") }
    val girilenTutarDouble = girilenTutar.toDoubleOrNull() ?: 0.0
    val hesaplananMiktar = if (guncelFiyat > 0) girilenTutarDouble / guncelFiyat else 0.0
    val bakiyeYeterliMi = girilenTutarDouble <= mevcutBakiye && girilenTutarDouble > 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = ikonResId),
                    contentDescription = isim,
                    modifier = Modifier.size(50.dp).clip(CircleShape).background(acikGriArkaPlan).padding(4.dp)
                )
                Text(text = isim, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = koyuMetin)
                Text(text = "%.2f ₺".format(guncelFiyat), fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = acikGriArkaPlan, thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = girilenTutar,
                        onValueChange = { girilenTutar = it },
                        label = { Text("Tutar (₺)") },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = acikYesil,
                            unfocusedIndicatorColor = Color.LightGray,
                            focusedTextColor = koyuMetin,
                            unfocusedTextColor = koyuMetin,
                            focusedContainerColor = acikGriArkaPlan,
                            unfocusedContainerColor = acikGriArkaPlan
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.width(200.dp)
                    )
                    Text(text = "Yaklaşık: %.4f %s".format(hesaplananMiktar, birim), fontSize = 14.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = anaYesil),
                    onClick = {
                        if (bakiyeYeterliMi) {
                            butonAl(girilenTutarDouble, hesaplananMiktar)
                            girilenTutar = "" // İşlem sonrası alanı temizle
                        }
                    },
                    modifier = Modifier.height(56.dp),
                    enabled = bakiyeYeterliMi
                ) {
                    Text(text = "Al")
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

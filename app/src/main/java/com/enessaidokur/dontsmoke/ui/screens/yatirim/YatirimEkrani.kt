package com.enessaidokur.dontsmoke.ui.screens.yatirim

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.enessaidokur.dontsmoke.R
// Eksik importlar eklendi
import com.enessaidokur.dontsmoke.data.KullaniciVeriRepository
import com.enessaidokur.dontsmoke.network.RetrofitInstance
import com.enessaidokur.dontsmoke.ui.components.acikGriArkaPlan
import com.enessaidokur.dontsmoke.ui.components.acikYesil
import com.enessaidokur.dontsmoke.ui.components.anaYesil
import com.enessaidokur.dontsmoke.ui.components.koyuMetin
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YatirimEkrani(
    // ViewModel'i ekrana dahil et
    yatirimViewModel: YatirimViewModel = viewModel(factory = YatirimViewModel.Factory(repository = getRepository()))
) {
    // ViewModel'deki durumu (UiState) dinle
    val uiState by yatirimViewModel.uiState.collectAsState()

    // --- SİSTEM ÇUBUĞU RENKLERİ ---
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = acikYesil, darkIcons = false)
        systemUiController.setNavigationBarColor(color = Color.White, darkIcons = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Yatırım") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = acikYesil,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = acikGriArkaPlan
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            // Yüklenme, Hata ve Başarı durumlarını ViewModel'den gelen verilere göre yönet
            when {
                uiState.isFiyatlarLoading -> CircularProgressIndicator(color = anaYesil)
                uiState.fiyatHataMesaji != null -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = uiState.fiyatHataMesaji!!,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { yatirimViewModel.fetchAnlikFiyatlar(true) }) {
                            Text("Tekrar Dene")
                        }
                    }
                }
                else -> YatirimIcerigi(uiState = uiState, onSatinAl = yatirimViewModel::satinAl)
            }
        }
    }
}

@Composable
fun YatirimIcerigi(uiState: YatirimUiState, onSatinAl: (Double, Double, String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Bakiye Kartı - Artık ViewModel'den gelen canlı veriyi gösteriyor
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = anaYesil),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Kullanılabilir Bakiye", fontSize = 16.sp, color = Color.White.copy(alpha = 0.8f))
                    // ViewModel'den gelen formatlanmış bakiyeyi kullan
                    Text(uiState.kullanilabilirBakiyeFormatli, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        // Yatırım Araçları (ViewModel'den gelen fiyatlarla)
        item {
            val fiyat = uiState.anlikFiyatlar["ALTIN"] ?: 0.0
            YatirimAraciKarti(
                isim = "Altın",
                guncelFiyat = fiyat,
                ikonResId = R.drawable.altin,
                birim = "gr",
                onSatinAl = { harcanan, miktar -> onSatinAl(harcanan, miktar, "ALTIN") }
            )
        }
        item {
            val fiyat = uiState.anlikFiyatlar["GUMUS"] ?: 0.0
            YatirimAraciKarti(
                isim = "Gümüş",
                guncelFiyat = fiyat,
                ikonResId = R.drawable.gumus,
                birim = "gr",
                onSatinAl = { harcanan, miktar -> onSatinAl(harcanan, miktar, "GUMUS") }
            )
        }
        item {
            val fiyat = uiState.anlikFiyatlar["USD"] ?: 0.0
            YatirimAraciKarti(
                isim = "Dolar",
                guncelFiyat = fiyat,
                ikonResId = R.drawable.dolar,
                birim = "$",
                onSatinAl = { harcanan, miktar -> onSatinAl(harcanan, miktar, "DOLAR") }
            )
        }
        item {
            val fiyat = uiState.anlikFiyatlar["EUR"] ?: 0.0
            YatirimAraciKarti(
                isim = "Euro",
                guncelFiyat = fiyat,
                ikonResId = R.drawable.euro,
                birim = "€",
                onSatinAl = { harcanan, miktar -> onSatinAl(harcanan, miktar, "EURO") }
            )
        }
        item {
            val fiyat = uiState.anlikFiyatlar["BTC"] ?: 0.0
            YatirimAraciKarti(
                isim = "Bitcoin",
                guncelFiyat = fiyat,
                ikonResId = R.drawable.bitcoin,
                birim = "BTC",
                onSatinAl = { harcanan, miktar -> onSatinAl(harcanan, miktar, "BTC") }
            )
        }
    }
}

@Composable
fun YatirimAraciKarti(
    isim: String,
    guncelFiyat: Double,
    @DrawableRes ikonResId: Int,
    birim: String,
    onSatinAl: (harcananTutar: Double, alinanMiktar: Double) -> Unit
) {
    var girilenTutar by remember { mutableStateOf("") }
    val girilenTutarDouble = girilenTutar.toDoubleOrNull() ?: 0.0
    val hesaplananMiktar = if (guncelFiyat > 0) girilenTutarDouble / guncelFiyat else 0.0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
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
                Text(
                    text = String.format(Locale.getDefault(), "%.2f ₺", guncelFiyat),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = acikGriArkaPlan, thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = girilenTutar,
                        onValueChange = { girilenTutar = it },
                        label = { Text("Tutar (₺)") },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = anaYesil,
                            unfocusedIndicatorColor = Color.LightGray,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            cursorColor = anaYesil,
                            focusedTextColor = Color.Black

                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = String.format(Locale.getDefault(), "Yaklaşık: %.4f %s", hesaplananMiktar, birim),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = anaYesil),
                    onClick = { 
                        onSatinAl(girilenTutarDouble, hesaplananMiktar)
                        girilenTutar = "" // Alanı temizle
                    },
                    modifier = Modifier.height(56.dp),
                    enabled = (girilenTutarDouble > 0)
                ) {
                    Text(text = "Al")
                }
            }
        }
    }
}

@Composable
private fun getRepository(): KullaniciVeriRepository {
    val context = androidx.compose.ui.platform.LocalContext.current
    return remember { KullaniciVeriRepository(context, RetrofitInstance.api) }
}

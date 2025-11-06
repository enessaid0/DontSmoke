package com.enessaidokur.dontsmoke.ui.screens

import DontSmokeTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.util.TypedValue
import com.enessaidokur.dontsmoke.R
import com.enessaidokur.dontsmoke.ui.components.acikGriArkaPlan
import com.enessaidokur.dontsmoke.ui.components.acikYesil
import com.enessaidokur.dontsmoke.ui.components.anaYesil
import com.enessaidokur.dontsmoke.ui.viewmodel.CuzdanViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuzdanEkrani(cuzdanViewModel: CuzdanViewModel) {
    val cuzdanState by cuzdanViewModel.uiState.collectAsState()
    val toplamVarlik = cuzdanState.bakiye + cuzdanState.urunler.sumOf { it.miktar * it.anlikFiyat }

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = acikYesil, darkIcons = false)
        systemUiController.setNavigationBarColor(color = Color.Transparent, darkIcons = true)
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = acikYesil
                )
            )
        },
        containerColor = acikGriArkaPlan
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = anaYesil),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Toplam Varlık", fontSize = 16.sp, color = Color.White.copy(alpha = 0.8f))
                        Text("₺%.2f".format(toplamVarlik), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(color = Color.White.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Kullanılabilir Bakiye: ₺%.2f".format(cuzdanState.bakiye), fontSize = 14.sp, color = Color.White)
                    }
                }
            }

            item {
                Text(
                    text = "Varlıklarım", color =acikYesil,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
            }

            items(cuzdanState.urunler) { urun ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = urun.ikonResId),
                            contentDescription = urun.isim,
                            modifier = Modifier.size(48.dp).clip(CircleShape).background(acikGriArkaPlan.copy(alpha = 0.5f)).padding(6.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = urun.isim,
                            color=acikYesil,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.weight(1f))
                        
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                "%.4f %s".format(urun.miktar,urun.birim),
                                color = Color.Gray, 
                                fontSize = 14.sp
                            )
                            Text(
                                "₺%.2f".format(urun.miktar * urun.anlikFiyat), 
                                color = anaYesil, 
                                fontWeight = FontWeight.SemiBold, 
                                fontSize = 16.sp
                            )                        
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CuzdanEkraniPreview() {
    val previewViewModel = CuzdanViewModel()
    // Önizleme için örnek veriler ekleyelim
    previewViewModel.satinAl("Altın", 0.5, 2521.0, "gr", 5042.0, R.drawable.altin)
    previewViewModel.satinAl("Dolar", 100.0, 4184.0, "$", 41.84, R.drawable.dolar)
    previewViewModel.satinAl("Bitcoin", 0.001, 4524.92, "BTC", 4524920.73, R.drawable.bitcoin)

    DontSmokeTheme {
        CuzdanEkrani(cuzdanViewModel = previewViewModel)
    }
}

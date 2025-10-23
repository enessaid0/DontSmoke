package com.enessaidokur.dontsmoke.ui.screens

import DontSmokeTheme


import androidx.compose.foundation.layout.Box



import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar // 1. Değişiklik: Doğru import
import androidx.compose.material3.NavigationBarItem // 2. Değişiklik: Doğru import
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.enessaidokur.dontsmoke.R // Kendi ikonların için R dosyasını import etmelisin
import com.enessaidokur.dontsmoke.ui.components.GriArkaPlan
import com.enessaidokur.dontsmoke.ui.components.acikGriArkaPlan
import com.enessaidokur.dontsmoke.ui.components.anaYesil

import com.enessaidokur.dontsmoke.ui.navigation.BottomNavigationBar
import com.google.accompanist.systemuicontroller.rememberSystemUiController




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnaSayfaEkrani() {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        // 1. SADECE ÜST BAR (Status Bar)
        // Burayı 'acikYesil' yap
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = true  // Arka plan açık olduğu için ikonlar (saat/pil) koyu
        )

        // 2. SADECE ALT BAR (Sistem Gezinme Çubuğu)
        // Burayı 'anaYesil' yap ki BottomBar ile birleşsin
        systemUiController.setNavigationBarColor(
            color = Color.Transparent,
            darkIcons = false // Arka plan koyu olduğu için ikon (çizgi) açık renk
        )
    }
    // SCAFFOLD (İSKELE)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "İlerleme",
                        color = Color.White, // Yazı rengi
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                actions = {
                    IconButton(onClick = { /* TODO: Menü tıklandı */ }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menü",
                            tint = Color.White // İkon rengi
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = anaYesil
                )
            )
        },

        // 2. ALT BAR
        bottomBar = {
            BottomNavigationBar(
                // Bu ekran "İlerleme" ekranı olduğu için,
                // NavController'dan "ana_sayfa_ekrani"nın geldiğini varsayıyoruz.
                currentRoute = "ana_sayfa_ekrani",

                onNavigate = { yeniRota ->
                    // TODO: NavController'ı kullanarak 'yeniRota'ya git
                    // Örn: navController.navigate(yeniRota)
                }
            )
        },

        // Ana İçerik Alanı
        containerColor = GriArkaPlan

    ) { innerPadding ->

        // İçerik (Kartlar vb.)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Zorunlu padding
        ) {
            // Bir sonraki adımda içerik buraya gelecek
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DontSmokeTheme {
        AnaSayfaEkrani()

    }
}
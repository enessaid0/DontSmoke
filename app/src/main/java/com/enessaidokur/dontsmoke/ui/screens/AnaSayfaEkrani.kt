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
import com.enessaidokur.dontsmoke.ui.components.acikGriArkaPlan
import com.enessaidokur.dontsmoke.ui.components.statusbarYesili
import com.google.accompanist.systemuicontroller.rememberSystemUiController




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnaSayfaEkrani() {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        // Status bar'ın arkasını şeffaf yapmayı sağlıyor Seyidim.
        systemUiController.setSystemBarsColor(
            color = statusbarYesili,
            darkIcons = true
        )}
    // SCAFFOLD (İSKELE)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "İlerlemece",
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
                    containerColor = acikGriArkaPlan
                )
            )
        },
        // Ana İçerik Alanı Rengi AÇIK GRİ
        containerColor = acikGriArkaPlan

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

@Composable
fun BottomNavigationBar() {
    // 3. Değişiklik: BottomNavigation -> NavigationBar
    NavigationBar(
        containerColor = Color(0xFF8E44AD).copy(alpha = 0.8f) // Temaya uygun bir renk
    ) {
        // 4. Değişiklik: BottomNavigationItem -> NavigationBarItem
        NavigationBarItem(
            icon = {
                // Kendi ikonunu kullanmak istersen:
                Icon(painter = painterResource(id = R.drawable.home), contentDescription = "Ana Sayfa")
                // Veya Material ikonlarını kullanmak istersen:
                // Icon(Icons.Filled.Home, contentDescription = "Ana Sayfa")
            },
            label = { Text("İlerleme") },
            selected = true, // Bu değer dinamik olmalı
            onClick = { /* Ana sayfaya git */ }
        )
        NavigationBarItem(
            icon = {
                Icon(painter = painterResource(id = R.drawable.hearth),contentDescription = "Yatırım")
                // Icon(Icons.Filled.MonetizationOn, contentDescription = "Yatırım")
            },
            label = { Text("Yatırım") },
            selected = false,
            onClick = { /* Yatırım sayfasına git */ }
        )
        NavigationBarItem(
            icon = {
                Icon(painter = painterResource(id = R.drawable.money), contentDescription = "Sağlık")
                // Icon(Icons.Filled.VolunteerActivism, contentDescription = "Sağlık")
            },
            label = { Text("Sağlık") },
            selected = false,
            onClick = { /* Sağlık sayfasına git */ }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DontSmokeTheme {
        AnaSayfaEkrani()

    }
}
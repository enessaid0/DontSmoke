package com.enessaidokur.dontsmoke.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Renkleri tanımlayalım
private val colorPurple = Color(0xFF8A78F0)
private val colorPink = Color(0xFFD4779A)
private val colorPeach = Color(0xFFF89D60)

 val darkTextColor = Color(0xFF3B2E58)
 val buttonColor = Color(0xFF8A78F0)

 val anaYesil = Color(0xFF03884B) // Ana Yeşil (TopAppBar için)

 val acıkyesil = Color(0xFF4CAF50) // Açık Yeşil (Status Bar için)
 val acikGriArkaPlan = Color(0xFFEEE9E9)
 val GriArkaPlan = Color(0xFFF6F0F0)
 val systembarcubuguRengi = Color(0xFF212121)

@Composable
fun ArkaPlanBackground(content: @Composable () -> Unit) {

    // Renk duraklarını tanımlıyoruz
    // 0.0f -> %0 (Başlangıç), 1.0f -> %100 (Bitiş)
    val colorStops = listOf(
        0.0f to colorPurple, // Mor, %0 noktasında başlasın
        0.45f to colorPink,   // Pembe, %40 noktasında tam rengine ulaşsın
        1.0f to colorPeach   // Turuncu, %100 noktasında tam rengine ulaşsın
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                // Brush.linearGradient'in colorStops alanını kullanan versiyonu
                brush = Brush.linearGradient(
                    colorStops = colorStops.toTypedArray(),

                    // Yönü koruyoruz (Sol Üst -> Sağ Alt)
                    start = Offset(0f, 0f),
                    end = Offset.Infinite
                )
            )
    ) {
        content()
    }
}
package com.enessaidokur.dontsmoke.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enessaidokur.dontsmoke.R // <-- R dosyasını import et
import com.enessaidokur.dontsmoke.ui.components.ArkaPlanBackground
import com.enessaidokur.dontsmoke.ui.components.buttonColor
import com.enessaidokur.dontsmoke.ui.components.darkTextColor

// Diğer ekranlardan tanıdık renkler


@Composable
fun TebrikEkrani(onHadiBaslayalimClicked: () -> Unit) {

    ArkaPlanBackground {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            // --- YAZILAR (ÜSTTE) ---
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 70.dp, start = 32.dp, end = 32.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Tebrikler! 🎉", // Emoji ekledim
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = darkTextColor
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Artık daha sağlıklı bir\ngeleceğe adım atıyorsun.\nUnutma, her gün bir başarıdır!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = darkTextColor,
                    textAlign = TextAlign.Center
                )
            }


            // --- KARAKTER (ORTADA) ---
            Image(
                painter = painterResource(id = R.drawable.gulenkiz),
                contentDescription = "Tebrikler Karakteri",
                modifier = Modifier
                    .align(Alignment.BottomCenter) // Alta hizala
                    .padding(bottom = 100.dp) // Butonun üstünde durması için
                    .size(500.dp) // Boyutunu ayarla
            )

            // --- BUTON (EN ALTTA) ---
            Button(
                onClick = { onHadiBaslayalimClicked() },
                modifier = Modifier
                    .align(Alignment.BottomCenter) // Ekranın en altına hizala
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp) // Kenarlardan boşluk
                    .navigationBarsPadding()
                    .padding(bottom = 8.dp) // Telefondan alttan boşluk
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor // Tasarımdaki mor renk
                ),
                shape = RoundedCornerShape(21.dp)
            ) {
                Text(
                    text = "Hadi Başlayalım 🚀", // Emoji ekledim
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
        }
    }
}


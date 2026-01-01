package com.enessaidokur.dontsmoke.ui.screens.onboarding

// Gerekli tüm importları ekledim
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enessaidokur.dontsmoke.R // <-- R dosyasını import etmen gerekiyor
import com.enessaidokur.dontsmoke.ui.components.ArkaPlanBackground
import com.enessaidokur.dontsmoke.ui.components.buttonColor


@Composable
fun HosgeldinizEkrani(onIleriClicked: () -> Unit) {

    ArkaPlanBackground {

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {


            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter) // Box'ın üstüne hizala
                    .padding(top = 80.dp, start = 32.dp, end = 32.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Hoşgeldiniz!",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color =  Color(0xFF3B2E58)
                )
                Spacer(modifier = Modifier.height(13.dp))
                Text(
                    text = "Sigarayı bırakma yolculuğuna \n birlikte çıkalım 💪",
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF5B4783)
                )
            }

            //  BEYAZ BUTON ALANI (ARKADA)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 8.dp)
                    .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp,topStart = 40.dp, topEnd = 30.dp)) // Yuvarlak köşeler

            ) {
                // İleri Butonu (Beyaz alanın içinde ortada)
                Button(
                    onClick = { onIleriClicked() },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor
                    ),
                    shape = RoundedCornerShape(21.dp)
                ) {
                    Text(text = "İleri", fontSize = 20.sp, color = Color.White)
                }
            }

            // --- ADAM KARAKTERİ (ÖNDE) ---
            Image(
                painter = painterResource(id = R.drawable.gulenadam),
                contentDescription = "Hoşgeldiniz Karakteri",
                modifier = Modifier
                    .align(Alignment.BottomCenter) // Alta hizala
                    .padding(bottom = 130.dp) // Beyaz alanın üzerine taşımak için
                    .size(500.dp), // Karakterin boyutu
                contentScale = ContentScale.Fit
            )
        }
    }
}

// Android Studio'da tasarımı görebilmen için bir Preview ekledim
@Preview(showBackground = true)
@Composable
fun HosgeldinizEkraniPreview() {
    HosgeldinizEkrani(onIleriClicked = {})
}
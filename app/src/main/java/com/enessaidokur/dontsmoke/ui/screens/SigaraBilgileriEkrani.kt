package com.enessaidokur.dontsmoke.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enessaidokur.dontsmoke.R // <-- R dosyasını import etmen gerekiyor
import com.enessaidokur.dontsmoke.ui.components.ArkaPlanBackground
import com.enessaidokur.dontsmoke.ui.components.buttonColor
import com.enessaidokur.dontsmoke.ui.components.darkTextColor
import com.enessaidokur.dontsmoke.ui.components.koyuMetin

// Tasarımdaki renkleri tanımlayalım


private val textFieldBackgroundColor = Color(0xFFF5F5F5) // Gri TextField arka planı

@Composable
fun SigaraBilgileriEkrani(onIleriClicked: (String, String, String) -> Unit) {
    var kacYildirIciliyor by remember { mutableStateOf("") }
    var gundeKacTaneIciliyor by remember { mutableStateOf("") }
    var kacTl by remember { mutableStateOf("") }


    ArkaPlanBackground {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp) // Buton için alttan boşluk
        ) {


            Text(
                text = "Sigara kullanım bilgilerin:",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = darkTextColor, // Rengi koyu yaptık
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 80.dp) // Baştan boşluk
            )

            // 2. BEYAZ KART
            Box(
                modifier = Modifier
                    .align(Alignment.Center) // Kartı ortala
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp) // Kartın kenarlardan boşluğu
                    .clip(RoundedCornerShape(24.dp)) // Yuvarlak köşeler
                    .background(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp), // Kartın iç boşluğu
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // --- "Kaç yıldır içiyorsun?" ---
                    Text(
                        text = "Kaç yıldır içiyorsun?",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(), // Yazıyı sola yasla
                        fontSize = 19.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = kacYildirIciliyor,
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = { kacYildirIciliyor = it },
                        placeholder = { Text("Örn: 5") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = koyuMetin,
                            unfocusedTextColor = koyuMetin,
                            unfocusedContainerColor = textFieldBackgroundColor, // Odaklanılmamışken arka plan rengi
                            focusedContainerColor = textFieldBackgroundColor,   // Odaklanılmışken arka plan rengi
                            focusedIndicatorColor = Color.Transparent,  // Alt çizgiyi kaldır
                            unfocusedIndicatorColor = Color.Transparent, // Alt çizgiyi kaldır
                            disabledIndicatorColor = Color.Transparent // Alt çizgiyi kaldır
                        ),
                        singleLine = true

                    )



                    // --- "Günde kaç tane içiyorsun?" ---
                    Text(
                        text = "Günde kaç paket içiyorsunuz?",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(), // Yazıyı sola yasla
                        fontSize = 19.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField( // OutlinedTextField yerine TextField
                        value = gundeKacTaneIciliyor,
                        onValueChange = { gundeKacTaneIciliyor = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Örn: 20") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = koyuMetin,
                            unfocusedTextColor = koyuMetin,
                            unfocusedContainerColor = textFieldBackgroundColor, // Odaklanılmamışken arka plan rengi
                            focusedContainerColor = textFieldBackgroundColor,   // Odaklanılmışken arka plan rengi
                            focusedIndicatorColor = Color.Transparent,  // Alt çizgiyi kaldır
                            unfocusedIndicatorColor = Color.Transparent, // Alt çizgiyi kaldır
                            disabledIndicatorColor = Color.Transparent // Alt çizgiyi kaldır
                        ),
                        singleLine = true
                    )
                    // --- "Günde kaç tane içiyorsun?" ---
                    Text(
                        text = "Bir sigara paketi kaç TL?",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(), // Yazıyı sola yasla
                        fontSize = 19.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField( // OutlinedTextField yerine TextField
                        value = kacTl,
                        onValueChange = { kacTl = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Örn: 100") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = koyuMetin,
                            unfocusedTextColor = koyuMetin,
                            unfocusedContainerColor = textFieldBackgroundColor, // Odaklanılmamışken arka plan rengi
                            focusedContainerColor = textFieldBackgroundColor,   // Odaklanılmışken arka plan rengi
                            focusedIndicatorColor = Color.Transparent,  // Alt çizgiyi kaldır
                            unfocusedIndicatorColor = Color.Transparent, // Alt çizgiyi kaldır
                            disabledIndicatorColor = Color.Transparent // Alt çizgiyi kaldır
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // --- İKONLAR ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.liste),
                            contentDescription = "Liste",
                            modifier = Modifier.size(140.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.sigara),
                            contentDescription = "Sigara",
                            modifier = Modifier.size(140.dp)
                        )
                    }
                }
            }

            // 3. İLERİ BUTONU (En altta)
            Button(
                onClick = { onIleriClicked(kacYildirIciliyor, gundeKacTaneIciliyor,kacTl) },
                modifier = Modifier
                    .align(Alignment.BottomCenter) // Ekranın en altına hizala
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .navigationBarsPadding()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor // Tasarımdaki mor renk
                ),
                shape = RoundedCornerShape(21.dp)
            ) {
                Text(text = "İleri", fontSize = 20.sp, color = Color.White)
            }
        }
    }
}


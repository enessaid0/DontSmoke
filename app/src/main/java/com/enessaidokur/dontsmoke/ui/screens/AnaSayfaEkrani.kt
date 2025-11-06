package com.enessaidokur.dontsmoke.ui.screens



import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enessaidokur.dontsmoke.R
import com.enessaidokur.dontsmoke.ui.components.acikGriArkaPlan
import com.enessaidokur.dontsmoke.ui.components.acikYesil
import com.enessaidokur.dontsmoke.ui.components.anaYesil
import com.enessaidokur.dontsmoke.ui.components.dividerColor
import com.enessaidokur.dontsmoke.ui.components.koyuMetin
import com.enessaidokur.dontsmoke.ui.components.progressGray
import com.google.accompanist.systemuicontroller.rememberSystemUiController




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnaSayfaEkrani() {

    // --- Sistem Barlarının Renk Ayarları (Aynı) ---
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = acikYesil, darkIcons = true)
        systemUiController.setNavigationBarColor(color = Color.Transparent, darkIcons = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("İlerleme", color = Color.White, fontWeight = FontWeight.Bold) },
                actions = { IconButton(onClick = { /* Menü */ }) { Icon(Icons.Default.Menu, "Menü", tint = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = anaYesil)
            )
        },

        containerColor = acikGriArkaPlan
    ) { innerPadding ->

        // --- Ana İçerik Alanı (Kaydırılabilir) ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp), // Sayfanın geneli için kenar boşluğu
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 1.Kart: Dairesel İlerleme
            DaireselIlerlemeKarti()

            // Kartlar arası boşluk
            Spacer(modifier = Modifier.height(16.dp))


            // 2.Kart: 2x2 İstatistik
            IstatistikKarti()


            // Alt tarafa fazladan boşluk (BottomBar'a yapışmasın diye)
            Spacer(modifier = Modifier.height(16.dp))

            // Bilgilendirme tabloları devamı
            GecmisIstatistikKarti()
            Spacer(modifier = Modifier.height(16.dp))

            // Bilgilendirme tabloları devamı
            GelecekTahminKarti()
        }
    }
}


//FONKSİYON 1:Ana İlerleme Kartı

@Composable
fun DaireselIlerlemeKarti() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Üst Satır: Gün 1 ve ... ikonu
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Gün 1", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = koyuMetin)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { /* TODO: Menü tıklandı */ }) {
                    Icon(imageVector = Icons.Default.MoreVert, "Seçenekler", tint = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Dairesel Çubuk
            IlerlemeCemberi(
                yuzde = 0.01f,
                yuzdeMetni = "0.10%",
                altMetin = "24 SAAT"
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Motto
            Text(text = "Herşeyi Başarabilirim!", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = koyuMetin)
        }
    }
}

// FONKSİYON 2: Dairesel Çubuğun Kendisi

@Composable
fun IlerlemeCemberi(
    yuzde: Float, yuzdeMetni: String, altMetin: String,
    boyut: Dp = 180.dp, kalinlik: Dp = 16.dp,
) {
    Box(modifier = Modifier.size(boyut), contentAlignment = Alignment.Center) {
        // Arka Plan Çemberi
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(color = progressGray, startAngle = -90f, sweepAngle = 360f, useCenter = false, style = Stroke(width = kalinlik.toPx(), cap = StrokeCap.Round))
        }
        // İlerleme Çemberi
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(color = anaYesil, startAngle = -90f, sweepAngle = 360 * yuzde, useCenter = false, style = Stroke(width = kalinlik.toPx(), cap = StrokeCap.Round))
        }
        // Ortadaki Metinler
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = yuzdeMetni, fontSize = 36.sp, fontWeight = FontWeight.Bold, color = anaYesil)
            Text(text = altMetin, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

// --- YENİ FONKSİYON 3: İstatistik Kartı (2x2 Grid) ---
@Composable
fun IstatistikKarti() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // 1. Üst Sıra (Sigarasız & Para)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    // Bu, dikey ayırıcı çizginin Row yüksekliğinde olmasını sağlar
                    .height(IntrinsicSize.Min)
            ) {

                BilgiKutusu(
                    baslik = "Sigarasız",
                    deger = "2d 5sn", // Bu veri ViewModel'den gelecek
                    iconResId = R.drawable.timer,
                    renk = anaYesil,
                    modifier = Modifier.weight(1f) // İki kutu da eşit yer kaplasın
                )
                // Dikey Ayırıcı
                Divider(
                    color = dividerColor,
                    modifier = Modifier
                        .fillMaxHeight() // Row yüksekliği boyunca uzan
                        .width(1.dp)
                )
                BilgiKutusu(
                    baslik = "Kazanılan Para",
                    deger = "0,15 ₺", // Bu veri ViewModel'den gelecek
                    iconResId = R.drawable.tutarpara,
                    renk = anaYesil,
                    modifier = Modifier.weight(1f)
                )
            }

            // Yatay Ayırıcı
            Divider(color = dividerColor, modifier = Modifier.height(1.dp))

            // 2. Alt Sıra (Hayat & İçilmeyen)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                BilgiKutusu(
                    baslik = "Kazanılan Hayat",
                    deger = "0d 19sn", // Bu veri ViewModel'den gelecek
                    iconResId = R.drawable.time,
                    renk = anaYesil,
                    modifier = Modifier.weight(1f)
                )
                // Dikey Ayırıcı
                Divider(
                    color = dividerColor,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )
                BilgiKutusu(
                    baslik = "İçilmeyen Sigaralar",
                    deger = "0", // Bu veri ViewModel'den gelecek
                    iconResId = R.drawable.smokefree,
                    renk = anaYesil,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

//  YENİ FONKSİYON 4:Tek bir bilgi kutusu (Tekrar kullanılabilir)
@Composable
fun BilgiKutusu(
    baslik: String,
    deger: String,
    iconResId: Int,
    renk: Color,
    modifier: Modifier = Modifier,
) {
    // Column, öğeleri (ikon, değer, başlık) dikeyde hizalar
    Column(
        modifier = modifier.padding(16.dp), // Kutunun iç boşluğu
        horizontalAlignment = Alignment.CenterHorizontally, // Her şeyi yatayda ortala
        verticalArrangement = Arrangement.Center // Her şeyi dikeyde ortala
    ) {
        Icon(
            modifier = Modifier.size(36.dp),
            painter = painterResource(id = iconResId),
            contentDescription = baslik,
            tint = Color.Gray // İkonlar grii
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = deger,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = renk // Değer metni (2d 5sn) yeşil
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = baslik,
            fontSize = 12.sp,
            color = Color.Gray // Başlık metni (Sigarasız) gri
        )
    }
}
// YENİ FONKSİYON 5:Geçmiş İstatistik Kartı (Statik)
@Composable
fun GecmisIstatistikKarti() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Sigara içtiğiniz dönemde: ",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = koyuMetin
            )
            Spacer(modifier = Modifier.height(16.dp))
            GecmisSatir(
                iconResId = R.drawable.sigaraicon,
                deger = "73.000",
                aciklama = "Sigara içildi."
            )
            GecmisSatir(
                iconResId = R.drawable.money,
                deger = "365.000",
                aciklama = "Para Harcandı."
            )
            GecmisSatir(
                iconResId = R.drawable.takvim,
                deger = "1Y 7A 17G",
                aciklama = "Hayat Kaybedildi."
            )

        }

    }
}
// YENİ FONKSİYON 6:Geçmiş İstatistik için Satır Composable'ı
@Composable
fun GecmisSatir(
    @DrawableRes iconResId: Int,
    deger: String,
    aciklama: String,
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically){
    Icon(
        painter = painterResource(id = iconResId),
        contentDescription = aciklama,
        tint = koyuMetin.copy(alpha = 0.8f),
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .padding(10.dp)
    )
    Spacer(modifier = Modifier.width(16.dp))
    Column{
        Text(text = deger, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = anaYesil)
        Text(text = aciklama, fontSize = 14.sp,color =Color.Gray )
}
    }
}
// YENİ FONKSİYON 7: Gelecek Tahminleri Kartı (Statik)
@Composable
fun GelecekTahminKarti() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Ne kadar kar elde edeceksiniz?", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = koyuMetin)
            Text(text = "Para ve Hayat Beklentisi", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            // Tablo Satırları (Statik)
            TahminSatiri(zaman = "1 Hafta", para = "700 ₺", hayat = "1g 1s 40d")
            Divider(color = dividerColor, thickness = 1.dp)
            TahminSatiri(zaman = "1 Ay", para = "3.000 ₺", hayat = "4g 14s 0d")
            Divider(color = dividerColor, thickness = 1.dp)
            TahminSatiri(zaman = "1 Yıl", para = "36.500 ₺", hayat = "1A 25g 18s")
            Divider(color = dividerColor, thickness = 1.dp)
            TahminSatiri(zaman = "5 Yıl", para = "182.500 ₺", hayat = "9A 8g 19s")
            Divider(color = dividerColor, thickness = 1.dp)
            TahminSatiri(zaman = "10 Yıl", para = "365.000 ₺", hayat = "1Y 6A 17g")
            Divider(color = dividerColor, thickness = 1.dp)
            TahminSatiri(zaman = "20 Yıl", para = "730.000 ₺", hayat = "3Y 1A 5g")
        }
    }
}


// YENİ FONKSİYON 8:Gelecek Tahminleri için Satır Composable'ı

@Composable
fun TahminSatiri(zaman: String, para: String, hayat: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = zaman, modifier = Modifier.weight(1.2f), fontSize = 14.sp, color = anaYesil)
        Text(text = para, modifier = Modifier.weight(1f), fontSize = 14.sp, color = anaYesil, textAlign = TextAlign.End)
        Text(text = hayat, modifier = Modifier.weight(1f), fontSize = 14.sp, color = anaYesil, textAlign = TextAlign.End)
    }
}


@Preview(showBackground = true)
@Composable
fun AnaSayfaEkraniPreview() {

}
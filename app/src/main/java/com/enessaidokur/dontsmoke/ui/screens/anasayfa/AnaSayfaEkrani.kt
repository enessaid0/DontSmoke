package com.enessaidokur.dontsmoke.ui.screens.anasayfa

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enessaidokur.dontsmoke.R
import com.enessaidokur.dontsmoke.ui.components.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController

// ARTIK BU EKRAN ÇOK DAHA TEMİZ!
// Tüm hesaplamalar ViewModel'e taşındı.

@Composable
fun AnaSayfaEkrani(viewModel: AnaSayfaViewModel) {

    // 1. ViewModel'deki arayüz durumunu (UiState) canlı olarak dinliyoruz.
    val uiState by viewModel.uiState.collectAsState()

    // 2. ARAYÜZ (UI) - Geri kalanı neredeyse aynı.
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = acikYesil, darkIcons = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(acikYesil) // Arka plan rengini buradan veriyoruz
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 3. Kartları, ViewModel'den gelen UiState verileriyle dolduruyoruz.
        DaireselIlerlemeKarti(
            gun = uiState.mevcutGun,
            ilerlemeYuzdesi = uiState.ilerlemeYuzdesi,
            altMetin = uiState.gecenSureFormatli.substringAfter("g ") // "1g 5s 12d 30sn" -> "5s 12d 30sn"
        )

        Spacer(modifier = Modifier.height(16.dp))

        IstatistikKarti(
            sigarasizGecenSure = uiState.gecenSureFormatli.substringBefore(" d"), // "1g 5s 12d 30sn" -> "1g 5s"
            kazanilanPara = uiState.birikenParaFormatli,
            kazanilanHayat = uiState.kazanilanHayatFormatli,
            icilmeyenSigara = uiState.icilmeyenSigara.toString()
        )

        Spacer(modifier = Modifier.height(16.dp))

        GecmisIstatistikKarti(
            toplamIcilen = uiState.gecmisToplamIcilen,
            toplamHarcanan = uiState.gecmisToplamHarcanan,
            kaybedilenHayat = uiState.gecmisKaybedilenHayat
        )

        Spacer(modifier = Modifier.height(16.dp))
        GelecekTahminKarti()
    }
}


// --- TÜM YARDIMCI COMPOSABLE'LAR (DEĞİŞİKLİK YOK) ---

@Composable
fun DaireselIlerlemeKarti(gun: Int, ilerlemeYuzdesi: Float, altMetin: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Gün ${gun + 1}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = koyuMetin)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { /* TODO */ }) { Icon(Icons.Default.MoreVert, "Seçenekler", tint = Color.Gray) }
            }
            Spacer(modifier = Modifier.height(16.dp))
            IlerlemeCemberi(
                yuzde = ilerlemeYuzdesi,
                yuzdeMetni = "${(ilerlemeYuzdesi * 100).toInt()}%",
                altMetin = altMetin
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Her Şeyi Başarabilirim!", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = koyuMetin)
        }
    }
}

@Composable
fun IlerlemeCemberi(
    yuzde: Float, yuzdeMetni: String, altMetin: String,
    boyut: Dp = 180.dp, kalinlik: Dp = 16.dp,
) {
    Box(modifier = Modifier.size(boyut), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(color = progressGray, startAngle = -90f, sweepAngle = 360f, useCenter = false, style = Stroke(width = kalinlik.toPx(), cap = StrokeCap.Round))
        }
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(color = anaYesil, startAngle = -90f, sweepAngle = 360 * yuzde, useCenter = false, style = Stroke(width = kalinlik.toPx(), cap = StrokeCap.Round))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = yuzdeMetni, fontSize = 36.sp, fontWeight = FontWeight.Bold, color = anaYesil)
            Text(text = altMetin, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun IstatistikKarti(
    sigarasizGecenSure: String,
    kazanilanPara: String,
    kazanilanHayat: String,
    icilmeyenSigara: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                BilgiKutusu(baslik = "Sigarasız", deger = sigarasizGecenSure, iconResId = R.drawable.timer, renk = anaYesil, modifier = Modifier.weight(1f))
                VerticalDivider(color = dividerColor)
                BilgiKutusu(baslik = "Kazanılan Para", deger = kazanilanPara, iconResId = R.drawable.tutarpara, renk = anaYesil, modifier = Modifier.weight(1f))
            }
            HorizontalDivider(color = dividerColor)
            Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                BilgiKutusu(baslik = "Kazanılan Hayat", deger = kazanilanHayat, iconResId = R.drawable.time, renk = anaYesil, modifier = Modifier.weight(1f))
                VerticalDivider(color = dividerColor)
                BilgiKutusu(baslik = "İçilmeyen Sigara", deger = icilmeyenSigara, iconResId = R.drawable.smokefree, renk = anaYesil, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun VerticalDivider(modifier: Modifier = Modifier, thickness: Dp = 1.dp, color: Color = MaterialTheme.colorScheme.outlineVariant) {
    Box(
        modifier
            .fillMaxHeight()
            .width(thickness)
            .background(color = color)
    )
}

@Composable
fun BilgiKutusu(
    baslik: String,
    deger: String,
    @DrawableRes iconResId: Int,
    renk: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(36.dp),
            painter = painterResource(id = iconResId),
            contentDescription = baslik,
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = deger, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = renk)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = baslik, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun GecmisIstatistikKarti(toplamIcilen: String, toplamHarcanan: String, kaybedilenHayat: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text(
                text = "Sigara içtiğiniz dönemde: ",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = koyuMetin
            )
            Spacer(modifier = Modifier.height(16.dp))
            GecmisSatir(iconResId = R.drawable.sigaraicon, deger = toplamIcilen, aciklama = "Sigara içildi.")
            GecmisSatir(iconResId = R.drawable.money, deger = "$toplamHarcanan ₺", aciklama = "Para Harcandı.")
            GecmisSatir(iconResId = R.drawable.takvim, deger = kaybedilenHayat, aciklama = "Hayat Kaybedildi.")
        }
    }
}

@Composable
fun GecmisSatir(
    @DrawableRes iconResId: Int,
    deger: String,
    aciklama: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = aciklama,
            tint = koyuMetin.copy(alpha = 0.8f),
            modifier = Modifier.size(48.dp).clip(CircleShape).padding(10.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = deger, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = anaYesil)
            Text(text = aciklama, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun GelecekTahminKarti() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text(text = "Ne kadar kar elde edeceksiniz?", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = koyuMetin)
            Text(text = "Para ve Hayat Beklentisi", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            TahminSatiri(zaman = "1 Hafta", para = "700 ₺", hayat = "1g 1s 40d")
            HorizontalDivider(color = dividerColor)
            TahminSatiri(zaman = "1 Ay", para = "3.000 ₺", hayat = "4g 14s 0d")
            HorizontalDivider(color = dividerColor)
            TahminSatiri(zaman = "1 Yıl", para = "36.500 ₺", hayat = "1A 25g 18s")
            HorizontalDivider(color = dividerColor)
            TahminSatiri(zaman = "5 Yıl", para = "182.500 ₺", hayat = "9A 8g 19s")
            HorizontalDivider(color = dividerColor)
            TahminSatiri(zaman = "10 Yıl", para = "365.000 ₺", hayat = "1Y 6A 17g")
            HorizontalDivider(color = dividerColor)
            TahminSatiri(zaman = "20 Yıl", para = "730.000 ₺", hayat = "3Y 1A 5g")
        }
    }
}

@Composable
fun TahminSatiri(zaman: String, para: String, hayat: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = zaman, modifier = Modifier.weight(1.2f), fontSize = 14.sp, color = koyuMetin.copy(alpha = 0.8f))
        Text(text = para, modifier = Modifier.weight(1f), fontSize = 14.sp, color = anaYesil, textAlign = TextAlign.End, fontWeight = FontWeight.SemiBold)
        Text(text = hayat, modifier = Modifier.weight(1f), fontSize = 14.sp, color = anaYesil, textAlign = TextAlign.End, fontWeight = FontWeight.SemiBold)
    }
}

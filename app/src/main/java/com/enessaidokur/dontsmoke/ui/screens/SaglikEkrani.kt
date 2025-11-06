package com.enessaidokur.dontsmoke.ui.screens

// --- GEREKLİ TÜM İMPORTLAR ---
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas // <-- foundation'dan
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.* // Box, Column, fillMaxSize vs.
import androidx.compose.material3.* // Scaffold, TopAppBar vs.
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.enessaidokur.dontsmoke.R

import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.enessaidokur.dontsmoke.ui.components.acikGriArkaPlan
import com.enessaidokur.dontsmoke.ui.components.acikYesil
import com.enessaidokur.dontsmoke.ui.components.anaYesil
import com.enessaidokur.dontsmoke.ui.components.koyuMetin
import com.enessaidokur.dontsmoke.ui.components.progressGray
import com.enessaidokur.dontsmoke.ui.navigation.Rotalar
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

data class SaglikHedefi(
    val sureEtiketi: String,
    val aciklama: String,
    val hedefMilisaniye: Long
)

// --- Hedef Listesi (Şimdilik burada) ---
val saglikHedefleriListesi = listOf(
    SaglikHedefi("2 Saat", "Kan basıncı ve kalp atış hızı düşer.", TimeUnit.HOURS.toMillis(2)),
    SaglikHedefi("24 Saat", "Sigara içmenin kalp krizi riskini artıran etkileri azalmaya başlar.", TimeUnit.HOURS.toMillis(24)),
    SaglikHedefi("48 Saat", "Sinir uçları yeniden canlanır: Tat ve koku duyularınız tekrar güçlenir.", TimeUnit.HOURS.toMillis(48)),
    SaglikHedefi("3-5 Gün", "Akciğerlerde iyileşme başlar: İltihaplanma azalır, nefes darlığı azalır.", TimeUnit.DAYS.toMillis(3)),
    SaglikHedefi("1 Hafta", "Kan dolaşımı düzenlenir: Yürüyüş ve egzersiz kolaylaşır.", TimeUnit.DAYS.toMillis(7)),
    SaglikHedefi("10 Gün", "Öksürük azalır: Akciğerler temizlenmeye başlar.", TimeUnit.DAYS.toMillis(10)),
    SaglikHedefi("~10 Gün", "Cilt görünümü iyileşir: Cilt daha sağlıklı ve parlak görünmeye başlar.", TimeUnit.DAYS.toMillis(10)),
    SaglikHedefi("2 Hafta", "Nefes alma kapasitesi ve fiziksel dayanıklılık artar.", TimeUnit.DAYS.toMillis(14)),
    SaglikHedefi("1 Ay", "Bağışıklık sistemi güçlenir: Enfeksiyonlara karşı direnç artar.", TimeUnit.DAYS.toMillis(30)),
    SaglikHedefi("~1 Ay", "Akciğer fonksiyonu iyileşir: Öksürük ve balgam neredeyse kaybolur.", TimeUnit.DAYS.toMillis(30)),
    SaglikHedefi("3 Ay", "Kan dolaşımı stabilizesi artar: Egzersiz dayanıklılığı artar.", TimeUnit.DAYS.toMillis(90)),
    SaglikHedefi("~3 Ay", "Cilt sağlığı iyileşir: Pürüzler, sivilceler ve kırışıklıklar azalır.", TimeUnit.DAYS.toMillis(90)),
    SaglikHedefi("6 Ay", "Solunum yolu enfeksiyonları riski azalır.", TimeUnit.DAYS.toMillis(180)),
    SaglikHedefi("~6 Ay", "Daha fazla enerji ve daha kaliteli uyku.", TimeUnit.DAYS.toMillis(180)),
    SaglikHedefi("1 Yıl", "Kalp hastalıkları riski %50 oranında azalır.", TimeUnit.DAYS.toMillis(365)),
    SaglikHedefi("5 Yıl", "Ağız, boğaz, akciğer ve pankreas kanseri riski yarıya iner.", TimeUnit.DAYS.toMillis(365 * 5)),
    SaglikHedefi("10 Yıl", "Akciğer kanseri riski %50 oranında azalır.", TimeUnit.DAYS.toMillis(365 * 10))
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaglikEkrani() {

    // --- Sistem Bar Ayarları ---
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = acikYesil, darkIcons = false)
        systemUiController.setNavigationBarColor(color = Color.Transparent, darkIcons = true)
    }
    Scaffold(
        // --- TOP BAR ---
        topBar = {
            KalpRitmiAppBar()
        },
        containerColor = acikGriArkaPlan

    ) { innerPadding ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
    saglikHedefleriListesi.forEachIndexed { index, hedef ->
 val ornekIlerleme = ((index + 1).toFloat() / saglikHedefleriListesi.size.toFloat()).coerceAtMost(1f)

        SaglikHedefKarti(
            // Süre etiketini açıklamanın başına biz ekliyoruz
            aciklama = "${hedef.sureEtiketi} sonrasında: ${hedef.aciklama}",
            ilerleme = ornekIlerleme // Şimdilik örnek ilerleme
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
        }
    }
}

//ÖZEL APP BAR FONKSİYONU
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KalpRitmiAppBar() {

    // --- Animasyon, Fırça, Resim (Bunlar aynı) ---
    val infiniteTransition = rememberInfiniteTransition(label = "kalpRitmiShimmer")
    val shimmerTranslateX by infiniteTransition.animateFloat(
        initialValue = -500f, // Animasyonun başlayacağı X konumu
        targetValue = 1500f, // Animasyonun gideceği X konumu
        animationSpec = infiniteRepeatable( // Animasyonun nasıl olacağı
            animation = tween(durationMillis = 2000, easing = LinearEasing), // 2 saniye sürsün
            repeatMode = RepeatMode.Restart // Başa sarıp tekrarlasın
        ),
        label = "shimmerTranslateX" // Animasyon etiketi (debug için)
    )
    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(0.0f),
            Color.White.copy(1.0f),
            Color.White.copy(1.0f),
            Color.White.copy(0.0f)
        ),
        start = Offset(shimmerTranslateX - 100f, 0f),
        end = Offset(shimmerTranslateX + 100f, 0f)
    )
    val ekgImageBitmap = ImageBitmap.imageResource(id = R.drawable.ekg) // <-- Kendi dosya adını kontrol et


    // TopAppBar ve Canvas'ı üst üste bindirmek için Box kullanıyoruz.
    // Bu Box, Scaffold'un topBar'ına verilecek olan şeydir.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp) // AppBar yüksekliği
            // BU ÖNEMLİ: Box'ın kendisinin Status Bar'ın altına inmesini sağlıyoruz

    ) {
        // 1. Katman: Arka Plan (TopAppBar gibi davranıyor)
        // Standart TopAppBar'ı KULLANMIYORUZ, sadece rengi çiziyoruz
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(anaYesil)
        )

        // 2. Katman: Canvas (EKG ve Işık)
        // Canvas bir Composable olduğu için Box içinde çağrılabilir.
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = 0.99f)
        ) {

            // EKG Resmi
            drawImage(
                image = ekgImageBitmap,
                dstSize = IntSize(size.width.toInt(), size.height.toInt()),
                alpha = 0.8f
            )

            // Hareketli Işık
            drawRect(
                brush = shimmerBrush,
                blendMode = BlendMode.SrcIn
            )
        }
    }
}

//FONKSİYON:Sağlık Hedef Kartı Şablonu
@Composable
private fun SaglikHedefKarti(
    ilerleme : Float,
    aciklama : String
){
    Card (
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 24.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
              text = aciklama,
              fontSize = 16.sp,
              color = koyuMetin,
              textAlign = TextAlign.Center,
              modifier = Modifier.padding(horizontal = 8.dp)
          )

            Spacer(modifier = Modifier.height(8.dp))

            // Yüzde Metni (Ortada)
            Text(
                // İlerlemeyi %100'e yuvarla, ondalık gösterme
                text = "${(ilerleme * 100).roundToInt()}%",
                fontSize = 16.sp, // Yüzdeyi büyüttük
                fontWeight = FontWeight.Bold,
                color = koyuMetin // Yüzdeyi de koyu yaptık
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ilerleme çubuğu
            LinearProgressIndicator(
                progress = {ilerleme},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp).clip(RoundedCornerShape(5.dp)),
                color = anaYesil, trackColor = progressGray, strokeCap = StrokeCap.Round)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Başarılar",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = koyuMetin
            )

        }
         }
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
fun SaglikEkraniPreview() {

}
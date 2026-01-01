package com.enessaidokur.dontsmoke.ui.screens.saglik

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enessaidokur.dontsmoke.R
import com.enessaidokur.dontsmoke.ui.components.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaglikEkrani(
    viewModel: SaglikEkraniViewModel,
    onFabClicked: () -> Unit // Navigasyon için eklendi
) {

    val saglikHedefleri by viewModel.uiState.collectAsState()

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = acikYesil, darkIcons = false)
        systemUiController.setNavigationBarColor(color = Color.Transparent, darkIcons = true)
    }

    Scaffold(
        topBar = { KalpRitmiAppBar() },
        containerColor = acikGriArkaPlan,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onFabClicked, // Tıklandığında navigasyonu tetikle
                containerColor = anaYesil,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.AutoAwesome, contentDescription = "Yapay Zeka Asistanı")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            saglikHedefleri.forEach { hedef ->
                SaglikHedefKarti(
                    aciklama = hedef.aciklama,
                    ilerleme = hedef.ilerleme
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KalpRitmiAppBar() {
    val infiniteTransition = rememberInfiniteTransition(label = "kalpRitmiShimmer")
    val shimmerTranslateX by infiniteTransition.animateFloat(
        initialValue = -500f,
        targetValue = 1500f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslateX"
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
    val ekgImageBitmap = ImageBitmap.imageResource(id = R.drawable.ekg)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(anaYesil)
        )
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = 0.99f)
        ) {
            drawImage(
                image = ekgImageBitmap,
                dstSize = IntSize(size.width.toInt(), size.height.toInt()),
                alpha = 0.8f
            )
            drawRect(
                brush = shimmerBrush,
                blendMode = BlendMode.SrcIn
            )
        }
    }
}

@Composable
private fun SaglikHedefKarti(
    ilerleme: Float,
    aciklama: String
) {
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
            Text(
                text = "${(ilerleme * 100).roundToInt()}%",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = koyuMetin
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { ilerleme },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = anaYesil, trackColor = progressGray, strokeCap = StrokeCap.Round
            )
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

package com.enessaidokur.dontsmoke.ui.screens.yapayzeka

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.enessaidokur.dontsmoke.network.yapayzeka.Message
import com.enessaidokur.dontsmoke.ui.components.acikYesil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YapayZekaSohbetEkrani(
    viewModel: YapayZekaSohbetViewModel,
    onGeriClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    val gosterilecekMesajlar = uiState.mesajlar.filter { it.role != "system" }

    LaunchedEffect(gosterilecekMesajlar.size) {
        if (gosterilecekMesajlar.isNotEmpty()) {
            listState.animateScrollToItem(gosterilecekMesajlar.size - 1)
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Yapay Zeka Asistanı") },
                navigationIcon = {
                    IconButton(onClick = onGeriClicked) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = acikYesil,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
            
        },
        bottomBar = {

            MesajGirisAlani(onMesajGonder = { viewModel.mesajGonder(it) }, yukleniyor = uiState.yukleniyor)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 8.dp)
            ) {
                items(gosterilecekMesajlar) { mesaj ->
                    MesajBalonu(mesaj = mesaj)
                }
            }
            uiState.hata?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun MesajBalonu(mesaj: Message) {
    val isUser = mesaj.role == "user"
    val alignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    val renk = if (isUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = alignment
    ) {
        Text(
            text = mesaj.content,
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(renk)
                .padding(12.dp)
                .widthIn(max = 300.dp),
        )
    }
}

@Composable
fun MesajGirisAlani(onMesajGonder: (String) -> Unit, yukleniyor: Boolean) {
    var metin by remember { mutableStateOf("") }

    Surface(shadowElevation = 8.dp, color = acikYesil) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = metin,
                onValueChange = { metin = it },
                placeholder = { Text("Mesajınızı yazın...") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
                enabled = !yukleniyor,
                // İmleç ve görünürlük sorununu çözmek için sabit renkler kullanıldı.
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White.copy(alpha = 0.9f),
                    disabledContainerColor = Color.LightGray,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            if (yukleniyor) {
                CircularProgressIndicator(modifier = Modifier.size(48.dp))
            } else {
                IconButton(
                    onClick = { if (metin.isNotBlank()) onMesajGonder(metin); metin = "" },
                    modifier = Modifier.size(48.dp),
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Gönder", tint = acikYesil)
                }
            }
        }
    }
}
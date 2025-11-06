package com.enessaidokur.dontsmoke.ui.viewmodel

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class YatirimUrunu(
    val isim: String,
    val miktar: Double,
    val birim: String,
    val anlikFiyat: Double,
    @DrawableRes val ikonResId: Int // İkon için Drawable resource ID'si
)

data class CuzdanUiState(
    val bakiye: Double = 100000.0, // Başlangıç bakiyesi
    val urunler: List<YatirimUrunu> = emptyList()
)

class CuzdanViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CuzdanUiState())
    val uiState: StateFlow<CuzdanUiState> = _uiState.asStateFlow()

    fun satinAl(isim: String, alinanMiktar: Double, harcananTutar: Double, birim: String, anlikFiyat: Double, @DrawableRes ikonResId: Int) {
        _uiState.update { currentState ->
            val yeniBakiye = currentState.bakiye - harcananTutar

            val mevcutUrunIndex = currentState.urunler.indexOfFirst { it.isim == isim }
            val guncelUrunler = currentState.urunler.toMutableList()

            if (mevcutUrunIndex != -1) {
                // Ürün zaten var, miktarını güncelle
                val mevcutUrun = guncelUrunler[mevcutUrunIndex]
                val yeniMiktar = mevcutUrun.miktar + alinanMiktar
                guncelUrunler[mevcutUrunIndex] = mevcutUrun.copy(miktar = yeniMiktar, anlikFiyat = anlikFiyat)
            } else {
                // Yeni ürün ekle
                guncelUrunler.add(YatirimUrunu(isim = isim, miktar = alinanMiktar, birim = birim, anlikFiyat = anlikFiyat, ikonResId = ikonResId))
            }

            currentState.copy(
                bakiye = yeniBakiye,
                urunler = guncelUrunler
            )
        }
    }
}
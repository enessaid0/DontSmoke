package com.enessaidokur.dontsmoke.ui.screens.cuzdan

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.enessaidokur.dontsmoke.R
import com.enessaidokur.dontsmoke.data.KullaniciVeriRepository
import com.enessaidokur.dontsmoke.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import kotlinx.coroutines.flow.collect

// Cüzdandaki her bir yatırım varlığını temsil eden veri sınıfı (ikon eklendi)
data class CuzdanVarlik(
    val kod: String,
    val ad: String,
    val miktar: Double,
    val anlikFiyat: Double,
    val toplamDeger: Double,
    @DrawableRes val ikonResId: Int
)

// Genişletilmiş UiState
data class CuzdanUiState(
    val birikenPara: Double = 0.0,
    val birikenParaFormatli: String = "0,00 ₺",
    val varliklar: List<CuzdanVarlik> = emptyList(),
    val toplamYatirimDegeri: Double = 0.0,
    val toplamCuzdanDegeri: Double = 0.0,
    val toplamCuzdanDegeriFormatli: String = "0,00 ₺",
    val isFiyatlarLoading: Boolean = false,
    val hataMesaji: String? = null
)

class CuzdanViewModel(private val repository: KullaniciVeriRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CuzdanUiState())
    val uiState: StateFlow<CuzdanUiState> = _uiState.asStateFlow()

    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("tr", "TR"))
    private var paraGuncellemeJob: Job? = null
    private val anlikFiyatlar = MutableStateFlow<Map<String, Double>>(emptyMap())

    init {
        observeKullaniciVerileriForBirikim()
        observeVarliklarVeFiyatlar()
        startPriceUpdates()
    }

    private fun startPriceUpdates() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchAnlikFiyatlar(showLoading = true)
            while (true) {
                delay(18000000L)
                fetchAnlikFiyatlar(showLoading = false)
            }
        }
    }

    private fun fetchAnlikFiyatlar(showLoading: Boolean) {
        if (showLoading) _uiState.update { it.copy(isFiyatlarLoading = true, hataMesaji = null) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                coroutineScope {
                    val goldRequest = async { RetrofitInstance.api.getGoldPrices() }
                    val silverRequest = async { RetrofitInstance.api.getSilverPrice() }
                    val currencyRequest = async { RetrofitInstance.api.getAllCurrencies() }
                    val cryptoRequest = async { RetrofitInstance.api.getCryptoPrices() }

                    val goldResponse = goldRequest.await()
                    val silverResponse = silverRequest.await()
                    val currencyResponse = currencyRequest.await()
                    val cryptoResponse = cryptoRequest.await()

                    val newPrices = mutableMapOf<String, Double>()
                    var usdTryRate = 0.0

                    currencyResponse.result.find { it.name.contains("USD", ignoreCase = true) }?.let {
                        val rate = it.selling?.replace(",", ".")?.toDoubleOrNull() ?: 0.0
                        newPrices["USD"] = rate
                        usdTryRate = rate
                    }
                    currencyResponse.result.find { it.name.contains("EUR", ignoreCase = true) }?.let {
                        newPrices["EUR"] = it.selling?.replace(",", ".")?.toDoubleOrNull() ?: 0.0
                    }
                    goldResponse.result.find { it.name.contains("Gram", ignoreCase = true) }?.let {
                        newPrices["ALTIN"] = it.buying ?: 0.0
                    }
                    newPrices["GUMUS"] = silverResponse.result.selling

                    cryptoResponse.result.find { it.code.equals("BTC", ignoreCase = true) }?.let { btcData ->
                        if (usdTryRate > 0) newPrices["BTC"] = btcData.price * usdTryRate
                    }

                    anlikFiyatlar.value = newPrices
                    _uiState.update { it.copy(isFiyatlarLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isFiyatlarLoading = false, hataMesaji = "Fiyatlar yüklenemedi.") }
            }
        }
    }

    private fun observeVarliklarVeFiyatlar() {
        viewModelScope.launch(Dispatchers.Default) {
            combine(
                repository.kullaniciVarliklari,
                anlikFiyatlar
            ) { kullaniciVarliklari, fiyatlar ->
                val cuzdanVarliklari = mutableListOf<CuzdanVarlik>()

                val altin = kullaniciVarliklari.altin
                val gumus = kullaniciVarliklari.gumus
                val dolar = kullaniciVarliklari.dolar
                val euro = kullaniciVarliklari.euro
                val btc = kullaniciVarliklari.btc

                fiyatlar["ALTIN"]?.let { fiyat -> if (altin > 0) cuzdanVarliklari.add(CuzdanVarlik("ALTIN", "Gram Altın", altin, fiyat, altin * fiyat, R.drawable.altin)) }
                fiyatlar["GUMUS"]?.let { fiyat -> if (gumus > 0) cuzdanVarliklari.add(CuzdanVarlik("GUMUS", "Gümüş", gumus, fiyat, gumus * fiyat, R.drawable.gumus)) }
                fiyatlar["USD"]?.let { fiyat -> if (dolar > 0) cuzdanVarliklari.add(CuzdanVarlik("DOLAR", "Dolar", dolar, fiyat, dolar * fiyat, R.drawable.dolar)) }
                fiyatlar["EUR"]?.let { fiyat -> if (euro > 0) cuzdanVarliklari.add(CuzdanVarlik("EURO", "Euro", euro, fiyat, euro * fiyat, R.drawable.euro)) }
                fiyatlar["BTC"]?.let { fiyat -> if (btc > 0) cuzdanVarliklari.add(CuzdanVarlik("BTC", "Bitcoin", btc, fiyat, btc * fiyat, R.drawable.bitcoin)) }

                val toplamYatirim = cuzdanVarliklari.sumOf { it.toplamDeger }

                _uiState.update {
                    it.copy(
                        varliklar = cuzdanVarliklari,
                        toplamYatirimDegeri = toplamYatirim
                    )
                }
            }.collect()
        }
    }

    private fun observeKullaniciVerileriForBirikim() {
        viewModelScope.launch(Dispatchers.Default) {
            combine(
                repository.birakmaTarihi,
                repository.gundeKacPaket,
                repository.paketFiyati
            ) { birakmaTarihi, gundeKacPaket, paketFiyati ->
                Triple(birakmaTarihi, gundeKacPaket, paketFiyati)
            }.collect { (birakmaTarihi, gundeKacPaket, paketFiyati) ->
                paraGuncellemeJob?.cancel()
                paraGuncellemeJob = viewModelScope.launch(Dispatchers.Default) {
                    val gunlukMaliyet = gundeKacPaket.toDouble() * paketFiyati.toDouble()
                    val saniyelikMaliyet = gunlukMaliyet / (24 * 60 * 60)

                    while (true) {
                        if (birakmaTarihi > 0) {
                            val gecenSureSaniye = (System.currentTimeMillis() - birakmaTarihi) / 1000
                            val anlikBirikenPara = gecenSureSaniye * saniyelikMaliyet

                            _uiState.update { currentState ->
                                currentState.copy(
                                    birikenPara = anlikBirikenPara,
                                    birikenParaFormatli = currencyFormatter.format(anlikBirikenPara),
                                    toplamCuzdanDegeri = anlikBirikenPara,
                                    toplamCuzdanDegeriFormatli = currencyFormatter.format(anlikBirikenPara)
                                )
                            }
                        }
                        delay(1000)
                    }
                }
            }
        }
    }

    companion object {
        fun Factory(repository: KullaniciVeriRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(CuzdanViewModel::class.java)) {
                        return CuzdanViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
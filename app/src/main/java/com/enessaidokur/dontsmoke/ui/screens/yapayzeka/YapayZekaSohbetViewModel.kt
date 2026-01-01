package com.enessaidokur.dontsmoke.ui.screens.yapayzeka

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.enessaidokur.dontsmoke.data.KullaniciVeriRepository
import com.enessaidokur.dontsmoke.network.yapayzeka.ChatRequest
import com.enessaidokur.dontsmoke.network.yapayzeka.Message
import com.enessaidokur.dontsmoke.network.yapayzeka.YapayZekaRetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Arayüzün durumunu temsil eden data class
data class YapayZekaSohbetUiState(
    val mesajlar: List<Message> = emptyList(),
    val yukleniyor: Boolean = false,
    val hata: String? = null
)

class YapayZekaSohbetViewModel(private val kullaniciVeriRepository: KullaniciVeriRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(YapayZekaSohbetUiState())
    val uiState: StateFlow<YapayZekaSohbetUiState> = _uiState.asStateFlow()

    // Yapay zekanın kimliğini ve görevini belirleyen sistem talimatı
    private val sistemMesaji = Message(
        role = "system",
        content = "Sen sigarayı bırakma konusunda uzman bir motivasyon koçusun. Adın 'Dumansız Hayat Asistanı'. Amacın, kullanıcıyı sigarayı bırakma sürecinde motive etmek, zorlandığı anlarda destek olmak ve ona bu süreçte eşlik etmek. Cevapların kısa, net, empatik ve cesaretlendirici olmalı. Kullanıcıya asla sigara içmesini önerme veya bu yönde bir imada bulunma. Her zaman pozitif ve destekleyici bir dil kullan. Kullanıcıya 'sen' diye hitap et."
    )

    init {
        // Sohbeti bir karşılama mesajı ile başlat
        val baslangicMesajlari = listOf(
            sistemMesaji,
            Message(role = "assistant", content = "Merhaba! Ben Dumansız Hayat Asistanı. Sigarayı bırakma yolculuğunda sana destek olmak için buradayım. Bugün nasıl hissediyorsun?")
        )
        _uiState.value = _uiState.value.copy(mesajlar = baslangicMesajlari)
    }

    fun mesajGonder(kullaniciMesaji: String) {
        viewModelScope.launch {
            // 1. Kullanıcının mesajını listeye ekle ve yükleniyor durumunu başlat
            val yeniMesaj = Message(role = "user", content = kullaniciMesaji)
            val mevcutMesajlar = _uiState.value.mesajlar + yeniMesaj
            _uiState.value = _uiState.value.copy(mesajlar = mevcutMesajlar, yukleniyor = true, hata = null)

            try {
                // 2. API isteğini gönder (MODEL GÜNCELLENDİ)
                val request = ChatRequest(
                    model = "deepseek/deepseek-chat", // Kullanılacak model güncellendi
                    messages = mevcutMesajlar
                )
                val response = YapayZekaRetrofitInstance.api.getChatCompletion(request)

                // 3. API'den gelen cevabı listeye ekle ve yükleniyor durumunu bitir
                if (response.choices.isNotEmpty()) {
                    val aiResponse = response.choices.first().message
                    val tumMesajlar = _uiState.value.mesajlar + aiResponse
                    _uiState.value = _uiState.value.copy(mesajlar = tumMesajlar, yukleniyor = false)
                } else {
                     _uiState.value = _uiState.value.copy(hata = "Yapay zeka bir cevap döndürmedi.", yukleniyor = false)
                }

            } catch (e: Exception) {
                // 4. Hata durumunda arayüzü güncelle
                _uiState.value = _uiState.value.copy(hata = "Bir sorun oluştu: ${e.message}", yukleniyor = false)
            }
        }
    }

    // ViewModel'i oluşturmak için Factory sınıfı
    class Factory(private val kullaniciVeriRepository: KullaniciVeriRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(YapayZekaSohbetViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return YapayZekaSohbetViewModel(kullaniciVeriRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

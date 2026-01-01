package com.enessaidokur.dontsmoke.ui.screens.anasayfa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.enessaidokur.dontsmoke.data.KullaniciVeriRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.floor

// UiState sınıfı aynı kalıyor, bir sorun yok.
data class AnaSayfaUiState(
    val gecenSureFormatli: String = "0g 0s 0d 0sn",
    val birikenParaFormatli: String = "0.00 ₺",
    val icilmeyenSigara: Int = 0,
    val kazanilanHayatFormatli: String = "0g 0s 0d",
    val ilerlemeYuzdesi: Float = 0f,
    val mevcutGun: Int = 0,
    val gecmisToplamIcilen: String = "0",
    val gecmisToplamHarcanan: String = "0 ₺",
    val gecmisKaybedilenHayat: String = "0Y 0A 0G"
)

// Ana ViewModel Sınıfı
class AnaSayfaViewModel(
    private val repository: KullaniciVeriRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnaSayfaUiState())
    val uiState: StateFlow<AnaSayfaUiState> = _uiState.asStateFlow()

    // Para formatlayıcıyı bir kere oluşturup tekrar tekrar kullanıyoruz.
    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("tr", "TR"))

    init {
        baslatCanliVeriAkisini()
    }

    private fun baslatCanliVeriAkisini() {
        viewModelScope.launch(Dispatchers.Default) {
            // combine bloğu, içindeki Flow'lardan herhangi biri değiştiğinde yeniden tetiklenir.
            combine(
                repository.birakmaTarihi,
                repository.gundeKacPaket,
                repository.paketFiyati
                // icilenYil flow'unu kaldırdık çünkü artık ona ihtiyacımız yok, anlık hesaplanacak
            ) { birakmaTarihi, gundePaket, paketFiyati ->
                // Veriler hazır olduğunda, saniyelik zamanlayıcıyı başlat.
                while (true) {
                    val simdikiZaman = System.currentTimeMillis()
                    val tumHesaplamalar = hesapla(birakmaTarihi, gundePaket.toDouble(), paketFiyati.toDouble(), simdikiZaman)
                    _uiState.value = tumHesaplamalar // Hesaplanan yeni durumu arayüze gönder.
                    delay(1000) // 1 saniye bekle.
                }
            }.collect {
                // Bu kısım normalde bir şey yapmaz, combine'ın çalışması için gereklidir.
            }
        }
    }

    // Hesaplama fonksiyonunu daha basit ve doğru tiplerle çalışacak şekilde güncelledik.
    private fun hesapla(birakmaTarihi: Long, gundeKacPaket: Double, paketFiyati: Double, simdikiZaman: Long): AnaSayfaUiState {
        if (birakmaTarihi <= 0) {
            return AnaSayfaUiState() // Bırakma tarihi yoksa, boş state döndür.
        }

        // --- CANLI SAYAÇ HESAPLAMALARI ---
        val gecenSureMillis = simdikiZaman - birakmaTarihi
        val toplamGecenSaniye = TimeUnit.MILLISECONDS.toSeconds(gecenSureMillis)
        val gun = TimeUnit.MILLISECONDS.toDays(gecenSureMillis)
        val saat = TimeUnit.MILLISECONDS.toHours(gecenSureMillis) % 24
        val dakika = TimeUnit.MILLISECONDS.toMinutes(gecenSureMillis) % 60
        val saniye = TimeUnit.MILLISECONDS.toSeconds(gecenSureMillis) % 60

        val gundeIcilenAdet = gundeKacPaket * BIR_PAKETTEKI_SIGARA_SAYISI
        val gunlukMaliyet = gundeKacPaket * paketFiyati
        val saniyelikKazanc = if (gunlukMaliyet > 0) gunlukMaliyet / (24 * 3600) else 0.0
        val saniyelikSigara = if (gundeIcilenAdet > 0) gundeIcilenAdet / (24.0 * 3600.0) else 0.0

        val hesaplananBirikenPara = toplamGecenSaniye * saniyelikKazanc
        val hesaplananIcilmeyenSigara = toplamGecenSaniye * saniyelikSigara

        val kazanilanToplamDakika = floor(hesaplananIcilmeyenSigara) * 11
        val kazanilanGun = (kazanilanToplamDakika / (60 * 24)).toInt()
        val kazanilanSaat = ((kazanilanToplamDakika / 60) % 24).toInt()
        val kazanilanDakika = (kazanilanToplamDakika % 60).toInt()

        return AnaSayfaUiState(
            gecenSureFormatli = "${gun}g ${saat}s ${dakika}d ${saniye}sn",
            birikenParaFormatli = currencyFormatter.format(hesaplananBirikenPara),
            icilmeyenSigara = floor(hesaplananIcilmeyenSigara).toInt(),
            kazanilanHayatFormatli = "${kazanilanGun}g ${kazanilanSaat}s ${kazanilanDakika}d",
            ilerlemeYuzdesi = (gun.toFloat() % 30) / 30f,
            mevcutGun = gun.toInt(),
            // Geçmiş hesaplamaları şimdilik basitleştiriyoruz, gerekirse sonra eklenir.
            gecmisToplamIcilen = "N/A",
            gecmisToplamHarcanan = "N/A",
            gecmisKaybedilenHayat = "N/A"
        )
    }

    // Factory kısmı aynı kalıyor, bir sorun yok.
    companion object {
        private const val BIR_PAKETTEKI_SIGARA_SAYISI = 20

        fun Factory(repository: KullaniciVeriRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(AnaSayfaViewModel::class.java)) {
                        return AnaSayfaViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}

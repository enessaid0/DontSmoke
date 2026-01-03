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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.floor
import kotlin.math.roundToInt

data class AnaSayfaUiState(
    val gecenSureFormatli: String = "0g 0s 0d 0sn",
    val birikenParaFormatli: String = "0.00 ₺",
    val icilmeyenSigara: Int = 0,
    val kazanilanHayatFormatli: String = "0g 0s 0d",
    val ilerlemeYuzdesi: Float = 0f,
    val mevcutGun: Int = 0,
    // Başlangıç değerlerini "0" olarak ayarlıyoruz, "N/A" değil.
    val gecmisToplamIcilen: String = "0",
    val gecmisToplamHarcanan: String = "0 ₺",
    val gecmisKaybedilenHayat: String = "0 gün"
)

// "N/A" SORUNUNU KESİN OLARAK ÇÖZEN SON ViewModel
class AnaSayfaViewModel(
    private val repository: KullaniciVeriRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnaSayfaUiState())
    val uiState: StateFlow<AnaSayfaUiState> = _uiState.asStateFlow()

    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("tr", "TR"))
    private val numberFormatter = NumberFormat.getNumberInstance(Locale("tr", "TR"))

    init {
        baslatCanliVeriAkisini()
    }

    private fun baslatCanliVeriAkisini() {
        viewModelScope.launch(Dispatchers.Default) {
            // Geçmiş hesaplamaları için 'icilenYil' flow'unu da dinlemek ZORUNDAYIZ.
            combine(
                repository.birakmaTarihi,
                repository.gundeKacPaket,
                repository.paketFiyati,
                repository.icilenYil // BU SATIR HAYATİ ÖNEMDE!
            ) { birakmaTarihi, gundePaket, paketFiyati, icilenYil ->
                // Bu blok, verilerden biri değiştiğinde veya ViewModel başladığında çalışır.
                // Sürekli güncellemeyi 'while(true)' döngüsü ile yapacağız.
                while (true) {
                    val simdikiZaman = System.currentTimeMillis()
                    val tumHesaplamalar = hesapla(
                        birakmaTarihi = birakmaTarihi,
                        gundeKacPaket = gundePaket.toDouble(),
                        paketFiyati = paketFiyati.toDouble(),
                        icilenYil = icilenYil.toDouble(), // Bu parametreyi hesapla fonksiyonuna gönderiyoruz
                        simdikiZaman = simdikiZaman
                    )
                    _uiState.value = tumHesaplamalar
                    delay(1000) // Her saniye döngüyü tekrarla
                }

            }.collect {
                // Combine'ı başlatmak için boş bir collect gerekli.
            }
        }
    }

    private fun hesapla(
        birakmaTarihi: Long,
        gundeKacPaket: Double,
        paketFiyati: Double,
        icilenYil: Double, // HESAPLAMA İÇİN BU PARAMETRE GEREKLİ
        simdikiZaman: Long
    ): AnaSayfaUiState {
        if (birakmaTarihi <= 0) {
            return AnaSayfaUiState() // Bırakma tarihi yoksa, tüm değerler "0" olan state döndürülür.
        }

        // --- CANLI SAYAÇ HESAPLAMALARI (Mevcut Kazanımlar) ---
        val gecenSureMillis = simdikiZaman - birakmaTarihi
        val toplamGecenSaniye = TimeUnit.MILLISECONDS.toSeconds(gecenSureMillis)
        val gun = TimeUnit.MILLISECONDS.toDays(gecenSureMillis)
        val saat = TimeUnit.MILLISECONDS.toHours(gecenSureMillis) % 24
        val dakika = TimeUnit.MILLISECONDS.toMinutes(gecenSureMillis) % 60
        val saniye = TimeUnit.MILLISECONDS.toSeconds(gecenSureMillis) % 60

        val gundeIcilenAdet = gundeKacPaket * 20
        val gunlukMaliyet = gundeKacPaket * paketFiyati
        val saniyelikKazanc = if (gunlukMaliyet > 0) gunlukMaliyet / (24 * 3600) else 0.0
        val saniyelikSigara = if (gundeIcilenAdet > 0) gundeIcilenAdet / (24.0 * 3600.0) else 0.0

        val hesaplananBirikenPara = toplamGecenSaniye * saniyelikKazanc
        val hesaplananIcilmeyenSigara = toplamGecenSaniye * saniyelikSigara

        val kazanilanToplamDakika = floor(hesaplananIcilmeyenSigara) * 11
        val kazanilanGun = (kazanilanToplamDakika / (60 * 24)).toInt()
        val kazanilanSaat = ((kazanilanToplamDakika / 60) % 24).toInt()
        val kazanilanDakika = (kazanilanToplamDakika % 60).toInt()

        // --- GEÇMİŞ HESAPLAMALARI ("N/A" YERİNE GERÇEK HESAP) ---
        val toplamIcUgun = icilenYil * 365
        val gecmisteIcilenToplamSigara = (toplamIcUgun * gundeKacPaket * 20).roundToInt()
        val gecmisteHarcananToplamPara = toplamIcUgun * gundeKacPaket * paketFiyati
        val kaybedilenToplamDakikaGecmis = gecmisteIcilenToplamSigara * 11
        val kaybedilenGunGecmis = kaybedilenToplamDakikaGecmis / (60 * 24.0) // sonucu double almak için 24.0

        // Tüm hesaplanmış verilerle dolu yeni UiState'i döndür.
        return AnaSayfaUiState(
            gecenSureFormatli = "${gun}g ${saat}s ${dakika}d ${saniye}sn",
            birikenParaFormatli = currencyFormatter.format(hesaplananBirikenPara),
            icilmeyenSigara = floor(hesaplananIcilmeyenSigara).toInt(),
            kazanilanHayatFormatli = "${kazanilanGun}g ${kazanilanSaat}s ${kazanilanDakika}d",
            // Bir gündeki toplam saniye sayısı 86400'dür.
// Gün içindeki ilerlemeyi, geçen saniyeye göre hesaplayalım.
            ilerlemeYuzdesi = (toplamGecenSaniye % 86400) / 86400f,

            mevcutGun = gun.toInt(),
            // DOĞRU VE HESAPLANMIŞ VERİLERİ BURAYA YERLEŞTİRİYORUZ
            gecmisToplamIcilen = numberFormatter.format(gecmisteIcilenToplamSigara),
            gecmisToplamHarcanan = "${numberFormatter.format(gecmisteHarcananToplamPara.roundToInt())} ₺",
            gecmisKaybedilenHayat = "${numberFormatter.format(kaybedilenGunGecmis.roundToInt())} gün"
        )
    }

    companion object {
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

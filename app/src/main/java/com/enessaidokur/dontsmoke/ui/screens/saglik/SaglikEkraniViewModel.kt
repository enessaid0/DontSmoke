package com.enessaidokur.dontsmoke.ui.screens.saglik

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.enessaidokur.dontsmoke.data.KullaniciVeriRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

// 1. Ekranın ihtiyacı olan tekil bir hedef verisini tutacak veri sınıfı
data class SaglikHedefi(
    val sureEtiketi: String,
    val aciklama: String,
    val hedefMilisaniye: Long
)

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
    SaglikHedefi("6 Ay", "Daha fazla enerji ve daha kaliteli uyku.", TimeUnit.DAYS.toMillis(180)),
    SaglikHedefi("1 Yıl", "Kalp hastalıkları riski %50 oranında azalır.", TimeUnit.DAYS.toMillis(365)),
    SaglikHedefi("5 Yıl", "Ağız, boğaz, akciğer ve pankreas kanseri riski yarıya iner.", TimeUnit.DAYS.toMillis(365 * 5)),
    SaglikHedefi("10 Yıl", "Akciğer kanseri riski %50 oranında azalır.", TimeUnit.DAYS.toMillis(365 * 10))
)


data class SaglikHedefiUiState(
    val aciklama: String,
    val ilerleme: Float // 0.0f ile 1.0f arasında bir değer
)

// 2. ViewModel Sınıfı
class SaglikEkraniViewModel(
    private val repository: KullaniciVeriRepository) : ViewModel() {

    // ViewModel'in değiştirebildiği, özel (private) state listesi
    private val _uiState = MutableStateFlow<List<SaglikHedefiUiState>>(emptyList())
    // Ekranın dinleyebileceği, halka açık (public) state listesi
    val uiState: StateFlow<List<SaglikHedefiUiState>> = _uiState.asStateFlow()

    init {
        // ViewModel oluşturulur oluşturulmaz, bırakma tarihini dinlemeye başla.
        viewModelScope.launch {
            repository.birakmaTarihi.collectLatest { birakmaTarihi ->
                // Bırakma tarihi her değiştiğinde (genellikle bir kere olur),
                // veya ilk defa alındığında, ilerlemeyi hesapla.
                hesaplaIlerleme(birakmaTarihi)
            }
        }
    }

    private fun hesaplaIlerleme(birakmaTarihi: Long) {
        if (birakmaTarihi <= 0) {
            _uiState.value = emptyList() // Bırakma tarihi yoksa boş liste göster.
            return
        }

        val gecenSureMillis = System.currentTimeMillis() - birakmaTarihi

        // Her bir sağlık hedefi için ilerlemeyi hesapla ve yeni bir liste oluştur.
        val guncelListe = saglikHedefleriListesi.map { hedef ->
            val ilerlemeYuzdesi = (gecenSureMillis.toFloat() / hedef.hedefMilisaniye.toFloat()).coerceAtMost(1f)

            SaglikHedefiUiState(
                // Açıklamayı ViewModel içinde birleştiriyoruz.
                aciklama = "${hedef.sureEtiketi} sonrasında: ${hedef.aciklama}",
                ilerleme = ilerlemeYuzdesi
            )
        }

        _uiState.value = guncelListe // Hesaplanan yeni listeyi arayüze gönder.
    }

    // Bu ViewModel'in de kendi Factory'si var.
    companion object {
        fun Factory(repository: KullaniciVeriRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(SaglikEkraniViewModel::class.java)) {
                        return SaglikEkraniViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}

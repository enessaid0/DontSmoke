package com.enessaidokur.dontsmoke.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.enessaidokur.dontsmoke.network.ApiService
import com.enessaidokur.dontsmoke.network.GoldPriceResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "kullanici_verileri")

data class KullaniciVarliklari(
    val altin: Double,
    val gumus: Double,
    val dolar: Double,
    val euro: Double, // Eklendi
    val btc: Double      // Eklendi
)

class KullaniciVeriRepository(
    private val context: Context,
    private val apiService: ApiService
) {

    private val dataStore = context.dataStore
    private val gson = Gson()

    companion object {
        val ONBOARDING_TAMAMLANDI = booleanPreferencesKey("onboarding_tamamlandi")
        val ICILEN_YIL = floatPreferencesKey("icilen_yil")
        val GUNDE_KAC_PAKET = floatPreferencesKey("gunde_kac_paket")
        val PAKET_FIYATI = floatPreferencesKey("paket_fiyati")
        val BIRAKMA_TARIHI = longPreferencesKey("birakma_tarihi")
        
        // Varlıklar
        val SAHIP_OLUNAN_ALTIN = doublePreferencesKey("sahip_olunan_altin")
        val SAHIP_OLUNAN_GUMUS = doublePreferencesKey("sahip_olunan_gumus")
        val SAHIP_OLUNAN_DOLAR = doublePreferencesKey("sahip_olunan_dolar")
        val SAHIP_OLUNAN_EURO = doublePreferencesKey("sahip_olunan_euro") // Eklendi
        val SAHIP_OLUNAN_BTC = doublePreferencesKey("sahip_olunan_btc")   // Eklendi
        
        // Harcama Takibi
        val TOPLAM_HARCANAN = doublePreferencesKey("toplam_harcanan") // Eklendi

        // Önbellek
        val SON_API_ISTEK_ZAMANI = longPreferencesKey("son_api_istek_zamani")
        val CACHED_DOVIZ_VERILERI = stringPreferencesKey("cached_doviz_verileri")
        val CACHE_SURESI_MILISANIYE = TimeUnit.HOURS.toMillis(5)
    }

    val onboardingTamamlandi: Flow<Boolean> = dataStore.data.map { it[ONBOARDING_TAMAMLANDI] ?: false }
    val birakmaTarihi: Flow<Long> = dataStore.data.map { it[BIRAKMA_TARIHI] ?: 0L }
    val icilenYil: Flow<Float> = dataStore.data.map { it[ICILEN_YIL] ?: 0f }
    val gundeKacPaket: Flow<Float> = dataStore.data.map { it[GUNDE_KAC_PAKET] ?: 0f }
    val paketFiyati: Flow<Float> = dataStore.data.map { it[PAKET_FIYATI] ?: 0f }

    // Harcama akışı eklendi
    val toplamHarcanan: Flow<Double> = dataStore.data.map { it[TOPLAM_HARCANAN] ?: 0.0 }

    val kullaniciVarliklari: Flow<KullaniciVarliklari> = dataStore.data.map { preferences ->
        KullaniciVarliklari(
            altin = preferences[SAHIP_OLUNAN_ALTIN] ?: 0.0,
            gumus = preferences[SAHIP_OLUNAN_GUMUS] ?: 0.0,
            dolar = preferences[SAHIP_OLUNAN_DOLAR] ?: 0.0,
            euro = preferences[SAHIP_OLUNAN_EURO] ?: 0.0, // Eklendi
            btc = preferences[SAHIP_OLUNAN_BTC] ?: 0.0      // Eklendi
        )
    }

    suspend fun getGuncelDovizVerileri(): List<GoldPriceResult> {
        val preferences = dataStore.data.first()
        val sonIstekZamani = preferences[SON_API_ISTEK_ZAMANI] ?: 0L
        val cachedVeriString = preferences[CACHED_DOVIZ_VERILERI]
        val suankiZaman = System.currentTimeMillis()

        if ((suankiZaman - sonIstekZamani > CACHE_SURESI_MILISANIYE) || cachedVeriString == null) {
            return try {
                val apiYaniti = apiService.getGoldPrices()
                val yeniVeriler = apiYaniti.result
                dataStore.edit { mutablePreferences ->
                    val veriJsonString = gson.toJson(yeniVeriler)
                    mutablePreferences[CACHED_DOVIZ_VERILERI] = veriJsonString
                    mutablePreferences[SON_API_ISTEK_ZAMANI] = suankiZaman
                }
                yeniVeriler
            } catch (e: Exception) {
                cachedVeriString?.let {
                    gson.fromJson(it, object : TypeToken<List<GoldPriceResult>>() {}.type)
                } ?: emptyList()
            }
        } else {
            return gson.fromJson(cachedVeriString, object : TypeToken<List<GoldPriceResult>>() {}.type)
        }
    }

    suspend fun kaydetSigaraBilgileri(gundeKacPaket: Float, paketFiyati: Float, icilenYil: Float) {
        dataStore.edit { preferences ->
            preferences[GUNDE_KAC_PAKET] = gundeKacPaket
            preferences[PAKET_FIYATI] = paketFiyati
            preferences[ICILEN_YIL] = icilenYil
            preferences[BIRAKMA_TARIHI] = System.currentTimeMillis()
        }
    }

    suspend fun onboardingiTamamla() {
        dataStore.edit { it[ONBOARDING_TAMAMLANDI] = true }
    }
    
    // Harcama ekleme fonksiyonu eklendi
    suspend fun harcamaEkle(harcananTutar: Double) {
        dataStore.edit {
            val mevcut = it[TOPLAM_HARCANAN] ?: 0.0
            it[TOPLAM_HARCANAN] = mevcut + harcananTutar
        }
    }

    suspend fun altinEkle(eklenecekMiktar: Double) {
        dataStore.edit {
            val mevcut = it[SAHIP_OLUNAN_ALTIN] ?: 0.0
            it[SAHIP_OLUNAN_ALTIN] = mevcut + eklenecekMiktar
        }
    }

    suspend fun gumusEkle(eklenecekMiktar: Double) {
        dataStore.edit {
            val mevcut = it[SAHIP_OLUNAN_GUMUS] ?: 0.0
            it[SAHIP_OLUNAN_GUMUS] = mevcut + eklenecekMiktar
        }
    }

    suspend fun dolarEkle(eklenecekMiktar: Double) {
        dataStore.edit {
            val mevcut = it[SAHIP_OLUNAN_DOLAR] ?: 0.0
            it[SAHIP_OLUNAN_DOLAR] = mevcut + eklenecekMiktar
        }
    }
    
    // Yeni varlıklar için fonksiyonlar eklendi
    suspend fun euroEkle(eklenecekMiktar: Double) {
        dataStore.edit {
            val mevcut = it[SAHIP_OLUNAN_EURO] ?: 0.0
            it[SAHIP_OLUNAN_EURO] = mevcut + eklenecekMiktar
        }
    }

    suspend fun btcEkle(eklenecekMiktar: Double) {
        dataStore.edit {
            val mevcut = it[SAHIP_OLUNAN_BTC] ?: 0.0
            it[SAHIP_OLUNAN_BTC] = mevcut + eklenecekMiktar
        }
    }
}
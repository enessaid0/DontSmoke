package com.enessaidokur.dontsmoke

import DontSmokeTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.enessaidokur.dontsmoke.data.KullaniciVeriRepository
import com.enessaidokur.dontsmoke.network.RetrofitInstance // Bu import gerekli
import com.enessaidokur.dontsmoke.ui.navigation.Navigasyon

class MainActivity : ComponentActivity() {

    // Hatanın olduğu satırı DÜZELTİYORUZ
    private val kullaniciVeriRepository by lazy {
        KullaniciVeriRepository(
            context = applicationContext,
            apiService = RetrofitInstance.api // Eksik olan parametreyi ekledik
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DontSmokeTheme {
                Navigasyon(kullaniciVeriRepository = kullaniciVeriRepository)
            }
        }
    }
}

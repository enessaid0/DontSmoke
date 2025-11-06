// MainActivity.kt DOSYASININ TAMAMI

package com.enessaidokur.dontsmoke

import DontSmokeTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.enessaidokur.dontsmoke.ui.navigation.Navigasyon
import com.enessaidokur.dontsmoke.ui.viewmodel.CuzdanViewModel

class MainActivity : ComponentActivity() {

    private val cuzdanViewModel by viewModels<CuzdanViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            DontSmokeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigasyon(cuzdanViewModel = cuzdanViewModel)
                }
            }
        }
    }
}
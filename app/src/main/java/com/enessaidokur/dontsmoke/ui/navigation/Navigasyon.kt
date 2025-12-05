package com.enessaidokur.dontsmoke.ui.navigation

import BottomNavigationBar
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.enessaidokur.dontsmoke.data.KullaniciVeriRepository
import com.enessaidokur.dontsmoke.ui.screens.anasayfa.AnaSayfaEkrani
import com.enessaidokur.dontsmoke.ui.screens.anasayfa.AnaSayfaViewModel
import com.enessaidokur.dontsmoke.ui.screens.cuzdan.CuzdanEkrani
import com.enessaidokur.dontsmoke.ui.screens.cuzdan.CuzdanViewModel
import com.enessaidokur.dontsmoke.ui.screens.giris.GirisEkrani
import com.enessaidokur.dontsmoke.ui.screens.onboarding.HosgeldinizEkrani
import com.enessaidokur.dontsmoke.ui.screens.onboarding.SigaraBilgileriEkrani
import com.enessaidokur.dontsmoke.ui.screens.onboarding.TebrikEkrani
import com.enessaidokur.dontsmoke.ui.screens.saglik.SaglikEkrani
import com.enessaidokur.dontsmoke.ui.screens.saglik.SaglikEkraniViewModel
import com.enessaidokur.dontsmoke.ui.screens.yatirim.YatirimEkrani
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Rotalar {
    const val GIRIS = "giris"
    const val HOSGELDINIZ = "hosgeldin"
    const val SIGARA_BILGILERI = "sigara_bilgileri"
    const val TEBRIK = "tebrikler"
    const val MAIN_APP_ROUTE = "main_app_route"
    const val ANA_SAYFA = "ana_sayfa"
    const val YATIRIM = "yatirim"
    const val SAGLIK = "saglik"
    const val CUZDAN = "cuzdan"
}

@Composable
fun Navigasyon(kullaniciVeriRepository: KullaniciVeriRepository) {
    val navController = rememberNavController()
    val onboardingTamamlandi by kullaniciVeriRepository.onboardingTamamlandi.collectAsState(initial = null)

    if (onboardingTamamlandi == null) {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {}
        return
    }

    val baslangicRotasi = if (onboardingTamamlandi == true) Rotalar.GIRIS else Rotalar.HOSGELDINIZ

    NavHost(
        navController = navController,
        startDestination = baslangicRotasi
    ) {
        composable(Rotalar.HOSGELDINIZ) {
            HosgeldinizEkrani(onIleriClicked = { navController.navigate(Rotalar.SIGARA_BILGILERI) })
        }
        composable(Rotalar.SIGARA_BILGILERI) {
            SigaraBilgileriEkrani(
                onIleriClicked = { gundeKacPaket, paketFiyati, icilenYil ->
                    CoroutineScope(Dispatchers.IO).launch {
                        kullaniciVeriRepository.kaydetSigaraBilgileri(gundeKacPaket.toFloat(), paketFiyati.toFloat(), icilenYil.toFloat())
                    }
                    navController.navigate(Rotalar.TEBRIK)
                }
            )
        }
        composable(Rotalar.TEBRIK) {
            TebrikEkrani(
                onHadiBaslayalimClicked = {
                    CoroutineScope(Dispatchers.IO).launch {
                        kullaniciVeriRepository.onboardingiTamamla()
                    }
                    navController.navigate(Rotalar.GIRIS) {
                        popUpTo(Rotalar.HOSGELDINIZ) { inclusive = true }
                    }
                }
            )
        }
        composable(Rotalar.GIRIS) {
            GirisEkrani(
                onGirisTamamlandi = {
                    navController.navigate(Rotalar.MAIN_APP_ROUTE) {
                        popUpTo(Rotalar.GIRIS) { inclusive = true }
                    }
                }
            )
        }
        composable(Rotalar.MAIN_APP_ROUTE) {
            MainAppScreen(kullaniciVeriRepository = kullaniciVeriRepository)
        }
    }
}

@Composable
fun MainAppScreen(kullaniciVeriRepository: KullaniciVeriRepository) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Tüm Fabrikaları (Usta Başılarını) burada bir kere oluşturuyoruz
    val anaSayfaViewModelFactory = AnaSayfaViewModel.Factory(kullaniciVeriRepository)
    //val yatirimViewModelFactory = YatirimViewModel.Factory(kullaniciVeriRepository, RetrofitInstance.api)
    val saglikEkraniViewModelFactory = SaglikEkraniViewModel.Factory(kullaniciVeriRepository)
    val cuzdanViewModelFactory = CuzdanViewModel.Factory(kullaniciVeriRepository)

    Scaffold(
        bottomBar = {
            if (currentRoute != null) {
                BottomNavigationBar(navController = navController, currentRoute = currentRoute)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Rotalar.ANA_SAYFA,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Rotalar.ANA_SAYFA) {
                val anaSayfaViewModel: AnaSayfaViewModel = viewModel(factory = anaSayfaViewModelFactory)
                AnaSayfaEkrani(viewModel = anaSayfaViewModel)
            }
            composable(Rotalar.YATIRIM) {
                // Artık ViewModel yok, direkt ekranı çağırıyoruz.
                YatirimEkrani()
            }
            composable(Rotalar.SAGLIK) {
                val saglikViewModel: SaglikEkraniViewModel = viewModel(factory = saglikEkraniViewModelFactory)
                SaglikEkrani(viewModel = saglikViewModel)
            }
            composable(Rotalar.CUZDAN) {
                val cuzdanViewModel: CuzdanViewModel = viewModel(factory = cuzdanViewModelFactory)
                CuzdanEkrani(viewModel = cuzdanViewModel)
            }
        }
    }
}

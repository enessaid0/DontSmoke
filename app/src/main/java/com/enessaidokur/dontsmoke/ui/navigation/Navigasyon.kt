package com.enessaidokur.dontsmoke.ui.navigation

import BottomNavigationBar
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.enessaidokur.dontsmoke.ui.screens.AnaSayfaEkrani
import com.enessaidokur.dontsmoke.ui.screens.CuzdanEkrani
import com.enessaidokur.dontsmoke.ui.screens.HosgeldinizEkrani
import com.enessaidokur.dontsmoke.ui.screens.SaglikEkrani
import com.enessaidokur.dontsmoke.ui.screens.SigaraBilgileriEkrani
import com.enessaidokur.dontsmoke.ui.screens.TebrikEkrani
import com.enessaidokur.dontsmoke.ui.screens.YatirimEkrani
import com.enessaidokur.dontsmoke.ui.viewmodel.CuzdanViewModel

object Rotalar {
    const val ONBOARDING_GRAPH = "onboarding_graph"
    const val MAIN_GRAPH = "main_graph"
    const val HOSGELDINIZ = "hosgeldin"
    const val SIGARA_BILGILERI = "sigara_bilgileri"
    const val TEBRIK = "tebrikler"
    const val ANA_SAYFA = "ana_sayfa"
    const val YATIRIM = "yatirim"
    const val SAGLIK = "saglik"
    const val CUZDAN = "cuzdan"
}

@Composable
fun Navigasyon(cuzdanViewModel: CuzdanViewModel) {
    val anaNavController = rememberNavController()

    NavHost(
        navController = anaNavController,
        startDestination = Rotalar.ONBOARDING_GRAPH
    ) {
        navigation(
            startDestination = Rotalar.HOSGELDINIZ,
            route = Rotalar.ONBOARDING_GRAPH
        ) {
            composable(Rotalar.HOSGELDINIZ) {
                HosgeldinizEkrani(
                    onIleriClicked = { anaNavController.navigate(Rotalar.SIGARA_BILGILERI) }
                )
            }
            composable(Rotalar.SIGARA_BILGILERI) {
                SigaraBilgileriEkrani(
                    onIleriClicked = { _, _, _ ->
                        anaNavController.navigate(Rotalar.TEBRIK)
                    }
                )
            }
            composable(Rotalar.TEBRIK) {
                TebrikEkrani(
                    onHadiBaslayalimClicked = {
                        anaNavController.navigate(Rotalar.MAIN_GRAPH) {
                            popUpTo(Rotalar.ONBOARDING_GRAPH) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(Rotalar.MAIN_GRAPH) {
            AnaUygulamaIcerigi(cuzdanViewModel = cuzdanViewModel)
        }
    }
}

@Composable
fun AnaUygulamaIcerigi(cuzdanViewModel: CuzdanViewModel) {
    val icNavController = rememberNavController()
    val navBackStackEntry by icNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = icNavController,
                currentRoute = currentRoute
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = icNavController,
            startDestination = Rotalar.ANA_SAYFA,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Rotalar.ANA_SAYFA) { AnaSayfaEkrani() } 
            composable(Rotalar.YATIRIM) { YatirimEkrani(cuzdanViewModel = cuzdanViewModel) }
            composable(Rotalar.SAGLIK) { SaglikEkrani() }
            composable(Rotalar.CUZDAN) { CuzdanEkrani(cuzdanViewModel = cuzdanViewModel) }
        }
    }
}
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.enessaidokur.dontsmoke.R
import com.enessaidokur.dontsmoke.ui.components.acikGriArkaPlan
import com.enessaidokur.dontsmoke.ui.components.anaYesil
import com.enessaidokur.dontsmoke.ui.navigation.Rotalar

// BU FONKSİYONUN TAMAMINI KENDİ KODUNUZDAKİYLE DEĞİŞTİRİN

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    currentRoute: String?, // 1. Değişiklik: Mevcut rotayı parametre olarak al
) {
    NavigationBar(
        containerColor = acikGriArkaPlan
    ) {
        // --- Cüzdan İTEMİ ---
        NavigationBarItem(
            icon = {
                Icon(modifier = Modifier.size(35.dp), painter = painterResource(id = R.drawable.cuzdan), contentDescription = "Yatırım", tint = anaYesil)
            },
            label = { Text("Cüzdan", color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Bold) },
            // 2. Değişiklik: Seçilme durumunu dinamik yap
            selected = currentRoute == Rotalar.CUZDAN,
            onClick = {
                // 3. Değişiklik: Navigasyon mantığını düzelt
                if (currentRoute != Rotalar.CUZDAN) {
                    navController.navigate(Rotalar.CUZDAN) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )


        // --- YATIRIM İTEMİ ---
        NavigationBarItem(
            icon = {
                Icon(modifier = Modifier.size(35.dp), painter = painterResource(id = R.drawable.money), contentDescription = "Yatırım", tint = anaYesil)
            },
            label = { Text("Yatırım", color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Bold) },
            // 2. Değişiklik: Seçilme durumunu dinamik yap
            selected = currentRoute == Rotalar.YATIRIM,
            onClick = {
                // 3. Değişiklik: Navigasyon mantığını düzelt
                if (currentRoute != Rotalar.YATIRIM) {
                    navController.navigate(Rotalar.YATIRIM) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )

        // --- ANA SAYFA İTEMİ ---
        NavigationBarItem(
            icon = {
                Icon(modifier = Modifier.size(35.dp), painter = painterResource(id = R.drawable.home), contentDescription = "Ana Sayfa", tint = anaYesil)
            },
            label = { Text("Ana sayfa", color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Bold) },
            // Seçilme durumunu dinamik yap
            selected = currentRoute == Rotalar.ANA_SAYFA,
            onClick = {
                if (currentRoute != Rotalar.ANA_SAYFA) {
                    navController.navigate(Rotalar.ANA_SAYFA) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )

        // --- SAĞLIK İTEMİ ---
        NavigationBarItem(
            icon = {
                Icon(modifier = Modifier.size(35.dp), painter = painterResource(id = R.drawable.hearth), contentDescription = "Sağlık", tint = anaYesil)
            },
            label = { Text("Sağlık", color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Bold) },
            // Seçilme durumunu dinamik yap
            selected = currentRoute == Rotalar.SAGLIK,
            onClick = {
                if (currentRoute != Rotalar.SAGLIK) {
                    navController.navigate(Rotalar.SAGLIK) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )
    }
}

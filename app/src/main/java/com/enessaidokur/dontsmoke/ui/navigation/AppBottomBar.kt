package com.enessaidokur.dontsmoke.ui.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.enessaidokur.dontsmoke.R
import com.enessaidokur.dontsmoke.ui.components.GriArkaPlan
import com.enessaidokur.dontsmoke.ui.components.acikGriArkaPlan
import com.enessaidokur.dontsmoke.ui.components.acıkyesil
import com.enessaidokur.dontsmoke.ui.components.anaYesil
import com.enessaidokur.dontsmoke.ui.components.systembarcubuguRengi

@Composable
fun BottomNavigationBar(
    currentRoute: String, // O an hangi ekranda olduğumuzu bilmek için
    onNavigate: (String) -> Unit, // Tıklanınca hangi ekrana gideceğimizi söylemek için
) {
    NavigationBar(

        containerColor = acikGriArkaPlan
    ) {

        NavigationBarItem(
            modifier = Modifier.padding(bottom = 0.dp),
            icon = {
                Icon(modifier = Modifier.size(35.dp),painter = painterResource(id = R.drawable.money),contentDescription = "Yatırım", tint = anaYesil)
                // Icon(Icons.Filled.MonetizationOn, contentDescription = "Yatırım")
            },
            label = { Text("Yatırım", color = Color.Black, fontSize = 15.sp,fontWeight = FontWeight.Bold) },
            selected = false,
            onClick = { /* Yatırım sayfasına git */ }
        )
        NavigationBarItem(
            icon = {
                Icon(modifier = Modifier.size(35.dp),painter = painterResource(id = R.drawable.home), contentDescription = "Ana Sayfa",tint = anaYesil)
                // Veya Material ikonlarını kullanmak istersen:
                // Icon(Icons.Filled.Home, contentDescription = "Ana Sayfa")
            },
            label = { Text("Ana sayfa",  color = Color.Black, fontSize = 15.sp,fontWeight = FontWeight.Bold) },
            selected = true,
            onClick = { /* Ana sayfaya git */ }
        )
        NavigationBarItem(
            icon = {
                Icon(modifier = Modifier.size(35.dp),painter = painterResource(id = R.drawable.hearth), contentDescription = "Sağlık",tint = anaYesil)
                // Icon(Icons.Filled.VolunteerActivism, contentDescription = "Sağlık")
            },
            label = { Text("Sağlık",color = Color.Black, fontSize = 15.sp,fontWeight = FontWeight.Bold) },
            selected = false,
            onClick = { /* Sağlık sayfasına git */ }
        )
    }
}
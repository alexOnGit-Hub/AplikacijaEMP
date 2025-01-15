package com.example.aplikacijaemp.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.aplikacijaemp.R

data class BottomNavItem(val route: String, val label: String, val icon: Int)

@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("home", "Home", R.drawable.home),
        BottomNavItem("cart", "Cart", R.drawable.cart),
        BottomNavItem("profile", "Profile", R.drawable.profile)
    )

    NavigationBar {
        val currentRoute = navController.currentBackStackEntryAsState()?.value?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) },
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

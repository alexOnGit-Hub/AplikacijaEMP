package com.example.aplikacijaemp.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

data class BottomNavItem(val route: String, val label: String, val icon: Int)

@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("home", "Home", android.R.drawable.ic_menu_view),
        BottomNavItem("cart", "Cart", android.R.drawable.ic_menu_add),
        BottomNavItem("profile", "Profile", android.R.drawable.ic_menu_myplaces)
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

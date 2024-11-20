package com.example.aplikacijaemp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aplikacijaemp.pages.LoginPage
import com.example.aplikacijaemp.pages.SingupPage
import com.example.aplikacijaemp.pages.StorePage

@Composable
fun Navigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login", builder = {
        composable("login") {
            LoginPage(modifier, navController, authViewModel)
        }
        composable("signup") {
            SingupPage(modifier, navController, authViewModel)
        }
        composable("home") {
            StorePage(modifier, navController, authViewModel)
        }
    })
}
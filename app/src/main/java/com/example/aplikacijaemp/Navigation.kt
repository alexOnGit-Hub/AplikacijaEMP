package com.example.aplikacijaemp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aplikacijaemp.pages.AddressCheckoutPage
import com.example.aplikacijaemp.pages.AdminPage
import com.example.aplikacijaemp.pages.CartPage
import com.example.aplikacijaemp.pages.CheckoutPage
import com.example.aplikacijaemp.pages.LoginPage
import com.example.aplikacijaemp.pages.OrderConfirmedPage
import com.example.aplikacijaemp.pages.PaymentPage
import com.example.aplikacijaemp.pages.ProfilePage
import com.example.aplikacijaemp.pages.SignupPage
import com.example.aplikacijaemp.pages.StorePage

@Composable
fun Navigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login", builder = {
        composable("login") {
            LoginPage(modifier, navController, authViewModel)
        }
        composable("signup") {
            SignupPage(modifier, navController, authViewModel)
        }
        composable("home") {
            StorePage(modifier, navController, authViewModel)
        }
        composable("profile") {
            ProfilePage(modifier, navController, authViewModel)
        }
        composable("cart") {
            CartPage(modifier, navController, authViewModel)
        }
        composable("admin") {
            AdminPage(modifier, navController, authViewModel)
        }
        composable("checkout") {
            CheckoutPage(modifier, navController, authViewModel)
        }
        composable("addressChechout") {
            AddressCheckoutPage(modifier, navController, authViewModel)
        }
        composable("payment") {
            PaymentPage(modifier, navController)
        }
        composable("order"){
            OrderConfirmedPage(modifier, navController, authViewModel)
        }
    })
}
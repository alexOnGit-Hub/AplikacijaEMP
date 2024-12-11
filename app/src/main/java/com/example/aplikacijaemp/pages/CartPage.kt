package com.example.aplikacijaemp.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.aplikacijaemp.AuthViewModel
import com.example.aplikacijaemp.navigation.BottomBar

@Composable
fun CartPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    Scaffold(
        bottomBar = { BottomBar(navController) } // Adding BottomBar here
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Cart Page",
                color = Color.Black
            )

            // Add additional cart-related content here
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCartPage() {
    val authViewModel = AuthViewModel()
    val navController = rememberNavController()

    CartPage(modifier = Modifier, navController = navController, authViewModel = authViewModel)
}

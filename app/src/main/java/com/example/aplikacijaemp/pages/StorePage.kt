package com.example.aplikacijaemp.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.aplikacijaemp.AuthState
import com.example.aplikacijaemp.AuthViewModel

@Composable
fun StorePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "StorePage")

        Button(onClick = {
            authViewModel.signout()
        }) {
            Text(text = "Sing out")
        }
    }
}
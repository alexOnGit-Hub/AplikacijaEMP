package com.example.aplikacijaemp.pages

import DatabaseHelper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.aplikacijaemp.AuthViewModel
import com.example.aplikacijaemp.R


@Composable
fun OrderConfirmedPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val dbHelper = DatabaseHelper(context)

    val userEmail = authViewModel.getUserEmail()


    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //TODO Fix gif animation
            AsyncImage(
                model = R.drawable.check_mark,
                contentDescription = "Order Confirmed",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 24.dp)
            )

            Text(
                text = "Your order has been confirmed!",
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    navController.navigate("home") {
                        if (userEmail != null) {
                            dbHelper.removeAllCartItemsForUser(userEmail)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(bottom = 20.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF33A1FF),
                    contentColor = Color.White
                )
            ) {
                Text(text = "Return to Shopping")
            }
        }
    }
}

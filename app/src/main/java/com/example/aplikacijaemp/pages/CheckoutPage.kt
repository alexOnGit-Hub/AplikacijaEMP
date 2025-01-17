package com.example.aplikacijaemp.pages

import DatabaseHelper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplikacijaemp.AuthViewModel
import com.example.aplikacijaemp.database.CartItem
import com.example.aplikacijaemp.navigation.BottomBar

@Composable
fun CheckoutPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val cartItems = remember { mutableStateListOf<CartItem>() }

    val userEmail = authViewModel.getUserEmail()

    val context = LocalContext.current
    val dbHelper = DatabaseHelper(context)

    LaunchedEffect(userEmail) {
        if (!userEmail.isNullOrEmpty()) {
            cartItems.clear()
            cartItems.addAll(dbHelper.getAllCartItemsForUser(userEmail))
        }
    }

    val totalPrice = "%.2f".format(cartItems.sumOf { it.price * it.quantity })

    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Checkout",
                modifier = Modifier.padding(16.dp),
                color = Color.Black,
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(cartItems) { cartItem ->
                    CardItemCheckout(cartItem = cartItem)
                }
            }

            Text(
                text = "Total Price: $totalPrice€",
                modifier = Modifier.padding(16.dp),
                color = Color.Black,
            )

            Button(
                onClick = { navController.navigate("addressChechout")  },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(45.dp),
                shape = RoundedCornerShape(12.dp), 
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xF133806D), 
                    contentColor = Color.White
                ),
            ) {
                Text(text = "Proceed to address")
            }
        }
    }
}

@Composable
fun CardItemCheckout(cartItem: CartItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp), 
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cartItem.name,
                    color = Color.Black,
                )
                Text(
                    text = "${cartItem.price}€ x ${cartItem.quantity}",
                )
            }

            Text(
                text = "${cartItem.price * cartItem.quantity}€",
            )
        }
    }
}

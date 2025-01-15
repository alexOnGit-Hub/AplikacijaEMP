package com.example.aplikacijaemp.pages

import DatabaseHelper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.aplikacijaemp.AuthViewModel
import com.example.aplikacijaemp.database.CartItem
import com.example.aplikacijaemp.navigation.BottomBar

@Composable
fun CartPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val dbHelper = DatabaseHelper(context)

    val userEmail = authViewModel.getUserEmail()

    val cartItems = remember { mutableStateListOf<CartItem>() }

    LaunchedEffect(userEmail) {
        if (!userEmail.isNullOrEmpty()) {
            cartItems.clear()
            cartItems.addAll(dbHelper.getAllCartItemsForUser(userEmail))
        }
    }

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
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cartItems) { cartItem ->
                    CartItemCard(
                        cartItem = cartItem,
                        onRemoveClick = {
                            if (userEmail != null) {
                                dbHelper.removeFromCart(userEmail, cartItem.id)
                            }
                            cartItems.remove(cartItem)
                        },
                        onUpdateQuantity = { newQuantity ->
                            if (userEmail != null) {
                                dbHelper.addToCart(userEmail, cartItem.id, newQuantity - cartItem.quantity)
                            }
                            cartItems[cartItems.indexOf(cartItem)] = cartItem.copy(quantity = newQuantity)
                        }
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(8.dp, top = 20.dp)
                        .aspectRatio(3f)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(10.dp))
                        .background(Color(0xFF90A5FF)),
                    contentAlignment = Alignment.Center
                ) {
                    val totalPrice = "%.2f".format(cartItems.sumOf { it.price * it.quantity })
                    val withoutDDV = "%.2f".format(cartItems.sumOf { it.price * it.quantity } * 0.78)

                    AlignedTextExample(withoutDDV, totalPrice)
                }

                Button(
                    onClick = { navController.navigate("checkout") },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(45.dp),
                    shape = RoundedCornerShape(12.dp), // Rounded corners for a softer look
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xF133806D), // A modern purple color (you can change this)
                        contentColor = Color.White // White text color
                    ),
                ) {
                    Text(
                        text = "Checkout",
                    )
                }

            }
        }
    }
}

@Composable
fun AlignedTextExample(withoutDDV: String, totalPrice: String) {
    Column(
        modifier = Modifier.fillMaxWidth(0.9f),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Without DDV:", modifier = Modifier.weight(1f), color = Color.Black)
            Text(text = withoutDDV, color = Color.Black)
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Total:", modifier = Modifier.weight(1f), color = Color.Black)
            Text(text = "$totalPrice€", color = Color.Black)
        }
    }
}

@Composable
fun CartItemCard(
    cartItem: CartItem,
    onRemoveClick: () -> Unit,
    onUpdateQuantity: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = cartItem.name, color = Color.Black)
                Text(text = "${cartItem.price}€ x ${cartItem.quantity}", color = Color.Gray)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { if (cartItem.quantity > 1) onUpdateQuantity(cartItem.quantity - 1) }) {
                    Text("-", color = Color.Black)
                }
                Text(text = "${cartItem.quantity}", color = Color.Black)
                IconButton(onClick = { onUpdateQuantity(cartItem.quantity + 1) }) {
                    Text("+", color = Color.Black)
                }
            }

            Button(onClick = onRemoveClick) {
                Text(text = "Remove")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCartPage() {
    val authViewModel = AuthViewModel() // Assuming this has the current user
    val navController = rememberNavController()

    CartPage(modifier = Modifier, navController = navController, authViewModel = authViewModel)
}

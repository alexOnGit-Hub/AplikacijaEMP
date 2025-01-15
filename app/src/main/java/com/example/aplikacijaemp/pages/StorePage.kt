package com.example.aplikacijaemp.pages

import DatabaseHelper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.aplikacijaemp.AuthViewModel
import com.example.aplikacijaemp.database.StoreItem
import com.example.aplikacijaemp.navigation.BottomBar

@Composable
fun StorePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val dbHelper = DatabaseHelper(context)

    val userEmail = authViewModel.getUserEmail()

    val items = dbHelper.getAllStoreItems()

    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Store Page",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .size(30.dp),
                color = Color.Black,
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { item ->
                    StoreItemCard(
                        item = item,
                        onAddToCartClick = { addedItem ->
                            if (userEmail != null) {
                                dbHelper.addToCart(storeItemId = addedItem.id, userEmail = userEmail, quantity = 1)
                            } else {
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun StoreItemCard(item: StoreItem, onAddToCartClick: (StoreItem) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.name, color = Color.Black)
                Text(text = "${item.price}€", color = Color.Gray)
            }

            Button(onClick = { onAddToCartClick(item) }) {
                Text(text = "Add to Cart")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStorePage() {
    val authViewModel = AuthViewModel()
    val navController = rememberNavController()

    StorePage(modifier = Modifier, navController = navController, authViewModel = authViewModel)
}

package com.example.aplikacijaemp.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.aplikacijaemp.AuthViewModel
import com.example.aplikacijaemp.navigation.BottomBar

// Data class for store items
data class StoreItem(
    val id: Int,
    val name: String,
    val price: Double
)

@Composable
fun StorePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val authState = authViewModel.authState.observeAsState()

    val sampleItems = listOf(
        StoreItem(1, "Smartphone", 699.99),
        StoreItem(2, "Laptop", 1299.99),
        StoreItem(3, "Headphones", 199.99),
        StoreItem(4, "Camera", 499.99)
    )

    val cart = remember { mutableStateListOf<StoreItem>() }

    Scaffold(
        bottomBar = { BottomBar(navController) } // Add BottomBar here
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
                modifier = Modifier.padding(16.dp),
                color = Color.Black
            )

            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sampleItems) { item ->
                    StoreItemCard(item = item, onAddToCartClick = { addedItem ->
                        cart.add(addedItem)
                    })
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

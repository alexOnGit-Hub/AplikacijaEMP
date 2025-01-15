package com.example.aplikacijaemp.pages

import DatabaseHelper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.aplikacijaemp.AuthViewModel
import com.example.aplikacijaemp.database.StoreItem


@Composable
fun AddImageButton(
    modifier: Modifier = Modifier,
    onAddImage: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Add Image")
        Button(onClick = onAddImage) {
            Text("Choose Image")
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val dbHelper = DatabaseHelper(context)

    var itemName by remember { mutableStateOf("") }
    var itemPrice by remember { mutableStateOf("") }

    var items = remember { mutableStateListOf<StoreItem>() }

    LaunchedEffect(true) {
        items.addAll(dbHelper.getAllStoreItems())
    }

    fun addItemToDatabase() {
        if (itemName.isNotEmpty() && itemPrice.isNotEmpty()) {
            val price = itemPrice.toDoubleOrNull()
            if (price != null) {
                val newItem = StoreItem(name = itemName, price = price)
                dbHelper.insertStoreItem(newItem)
                itemName = ""
                itemPrice = ""
                items.clear()
                items.addAll(dbHelper.getAllStoreItems())
            }
        }
    }

    fun deleteItemFromDatabase(itemId: Int) {
        dbHelper.deleteStoreItemById(itemId)
        items.removeIf { it.id == itemId }
    }

    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { Text("Item Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = itemPrice,
                    onValueChange = { itemPrice = it },
                    label = { Text("Item Price (€)") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { addItemToDatabase() }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                AddImageButton(onAddImage = {
                    // Open gallery or take a new photo here
                })

                Button(
                    modifier = Modifier.width(280.dp),
                    onClick = { addItemToDatabase() }) {
                    Text(text = "Add Item")
                }

                Button(
                    modifier = Modifier.width(280.dp),
                    onClick = {
                        navController.navigate("login")
                    }) {
                    Text(text = "Return to login")
                }


                Text(text = "Store Items:", color = Color.Black)

                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(items) { item ->
                        StoreItemCard(item = item, onDelete = { deleteItemFromDatabase(item.id) })
                    }
                }
            }
        }
    )
}

@Composable
fun StoreItemCard(
    item: StoreItem,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${item.name} - €${item.price}",
                modifier = Modifier.weight(1f),
            )
            Button(onClick = onDelete) {
                Text("Delete")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewAdminPage() {
    val authViewModel = AuthViewModel()
    val navController = rememberNavController()

    AdminPage(modifier = Modifier, navController = navController, authViewModel = authViewModel)
}

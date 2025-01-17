package com.example.aplikacijaemp.pages

import DatabaseHelper
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplikacijaemp.AuthViewModel
import com.example.aplikacijaemp.navigation.BottomBar

@Composable
fun NiceButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF33A1FF),
    textColor: Color = Color.White
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 5.dp, start = 30.dp, end = 30.dp)
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        )
    ) {
        Text(text = text, color = textColor)
    }
}

@Composable
fun AddressCheckoutPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val userEmail = authViewModel.getUserEmail()

    val context = LocalContext.current
    val dbHelper = DatabaseHelper(context)

    val street = remember { mutableStateOf("") }
    val city = remember { mutableStateOf("") }
    val postalCode = remember { mutableStateOf("") }
    val addressExists = remember { mutableStateOf(false) }

    val showAddressInput = remember { mutableStateOf(false) }

    LaunchedEffect(userEmail) {
        if (userEmail != null) {
            val address = dbHelper.getUserAddress(userEmail)
            address?.let {
                street.value = it.street
                city.value = it.city
                postalCode.value = it.postalCode
                addressExists.value = true
            } ?: run {
                addressExists.value = false
            }
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
            Text(
                text = if (addressExists.value) "Your Address" else "Add Address",
                modifier = Modifier.padding(16.dp),
                color = Color.Black
            )

            if (addressExists.value) {
                Text("Street: ${street.value}")
                Text("City: ${city.value}")
                Text("Postal Code: ${postalCode.value}")

                NiceButton(
                    text = "Change Address",
                    onClick = { showAddressInput.value = true },
                    backgroundColor = Color(0xFF2B66EF)
                )
            } else {
                NiceButton(
                    text = "Add Address",
                    onClick = { showAddressInput.value = true },
                    backgroundColor = Color(0xFF2B66EF)
                )
            }

            AnimatedVisibility(visible = showAddressInput.value) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = street.value,
                        onValueChange = { street.value = it },
                        placeholder = { Text("Street") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = city.value,
                        onValueChange = { city.value = it },
                        placeholder = { Text("City") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = postalCode.value,
                        onValueChange = { postalCode.value = it },
                        placeholder = { Text("Postal Code") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    NiceButton(
                        text = "Save Address",
                        onClick = {
                            val result = userEmail?.let {
                                dbHelper.updateUserAddress(
                                    email = it,
                                    street = street.value,
                                    city = city.value,
                                    postalCode = postalCode.value
                                )
                            }
                            if (result == 0) {
                                Toast.makeText(context, "Address updated!", Toast.LENGTH_SHORT).show()
                                showAddressInput.value = false
                                addressExists.value = true
                            } else if (result == 1) {
                                Toast.makeText(context, "Enter all fields.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Address added.", Toast.LENGTH_SHORT).show()
                                showAddressInput.value = false
                                addressExists.value = true
                            }
                        },
                        backgroundColor = Color(0xFF4CAF50) // Green for success
                    )
                }
            }

            if (addressExists.value) {
                Button(
                    onClick = { navController.navigate("payment")  },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(60.dp)
                        .padding(top = 10.dp),
                    shape = RoundedCornerShape(12.dp), // Rounded corners for a softer look
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xF133806D), // A modern purple color
                        contentColor = Color.White // White text color
                    ),
                ) {
                    Text(text = "Proceed to payment")
                }
            }
        }
    }
}

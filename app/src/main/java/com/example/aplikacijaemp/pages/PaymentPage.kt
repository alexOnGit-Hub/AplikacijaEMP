package com.example.aplikacijaemp.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplikacijaemp.R
import com.example.aplikacijaemp.navigation.BottomBar

// Enum to represent different payment methods
enum class PaymentMethod {
    CREDIT_CARD,
    PAYPAL,
    GOOGLE_PAY
}

@Composable
fun PaymentPage(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val selectedPaymentMethod = remember { mutableStateOf(PaymentMethod.CREDIT_CARD) }

    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select Payment Method",
                modifier = Modifier.padding(16.dp),
                color = Color.Black,
            )

            // Payment options with icons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                PaymentOptionWithIcon(
                    text = "Credit Card",
                    iconResId = R.drawable.ic_credit_card, 
                    isSelected = selectedPaymentMethod.value == PaymentMethod.CREDIT_CARD,
                    onSelected = { selectedPaymentMethod.value = PaymentMethod.CREDIT_CARD }
                )

                PaymentOptionWithIcon(
                    text = "PayPal",
                    iconResId = R.drawable.ic_paypal, 
                    isSelected = selectedPaymentMethod.value == PaymentMethod.PAYPAL,
                    onSelected = { selectedPaymentMethod.value = PaymentMethod.PAYPAL }
                )

                PaymentOptionWithIcon(
                    text = "Google Pay",
                    iconResId = R.drawable.ic_google_pay, 
                    isSelected = selectedPaymentMethod.value == PaymentMethod.GOOGLE_PAY,
                    onSelected = { selectedPaymentMethod.value = PaymentMethod.GOOGLE_PAY }
                )
            }

            Button(
                 onClick = { navController.navigate("order")
                },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(45.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xF133806D),
                    contentColor = Color.White
                ),
            ) {
                Text(text = "Confirm Payment Method")
            }
        }
    }
}

@Composable
fun PaymentOptionWithIcon(
    text: String,
    iconResId: Int,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .padding(start = 16.dp)
            .height(56.dp)
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(40.dp)
                .padding(end = 16.dp)
        )

        RadioButton(
            selected = isSelected,
            onClick = onSelected,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF33A1FF), 
                unselectedColor = Color.Gray
            )
        )

        Text(
            text = text,
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black
        )
    }
}

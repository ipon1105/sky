package com.example.sky.android.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun PrivacyPolicyScreen(navController: NavHostController) {
    val btnBackColor = Color(red = 0x00, green = 0x71, blue = 0xBC)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
            .padding(20.dp)
    ) {
        Row(horizontalArrangement = Arrangement.Start) {
            Image(
                painter = rememberVectorPainter(image = Icons.Filled.ArrowBack),
                contentDescription = " ",
                modifier = Modifier.clickable { navController.popBackStack() }
            )
        }

        Text(
            text = "Privacy Policy",
            color = btnBackColor,
            fontSize = 48.sp,
            modifier = Modifier.padding(top = 12.dp)
        )

        /*TODO: Добавить текст для Privacy Policy*/
        Text(
            text = "Privacy Policy Text.",
            color = Color.Gray,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}
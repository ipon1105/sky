package com.example.sky.android.screens

import com.example.sky.android.R
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp

@Composable
fun AccountScreen() {
    val id = remember{mutableStateOf(1)}
    val name = remember{mutableStateOf("myName")}
    val surname = remember{mutableStateOf("mySurname")}
    val phone_number = remember{mutableStateOf("myPhoneNumber")}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
            .padding(20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = rememberVectorPainter(image = Icons.Filled.Settings),
                contentDescription = "Icon back",
                modifier = Modifier.clickable { /*TODO: Сделать переход на настройки*/ }
            )
        }
        Image(painter = rememberVectorPainter(image = Icons.Filled.Settings), contentDescription = "My Photo")
        Text("Id = ${id.value}")
        Text("Name = ${name.value}")
        Text("Surname = ${surname.value}")
        Text("PhoneNumber = ${phone_number.value}")
    }
}
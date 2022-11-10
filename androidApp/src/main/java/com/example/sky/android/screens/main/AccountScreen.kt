package com.example.sky.android.screens

import com.example.sky.android.R
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AccountScreen() {
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

        AccountCard()
    }
}

@Composable
fun AccountCard(){

    val id = remember{mutableStateOf(1)}
    val nickname = remember{mutableStateOf("myNickname")}
    val phone_number = remember{mutableStateOf("myPhoneNumber")}

    Card(
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier.fillMaxWidth(), elevation = 5.dp){
        Row(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.account)),
                contentDescription = "My Photo",
                modifier = Modifier
                    .size(100.dp)
                    .padding(5.dp)
            )

            Column(modifier = Modifier.padding(top = 10.dp, start = 5.dp)) {

                Text(
                    text = "Nickname",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text("Id")
                Text("PhoneNumber")

            }
        }
    }
}
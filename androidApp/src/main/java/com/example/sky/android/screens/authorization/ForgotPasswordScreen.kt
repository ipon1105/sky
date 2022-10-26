package com.example.sky.android.screens

import com.example.sky.android.R
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun ForgotPasswordScreen(navController: NavHostController) {
    val btnBackColor = Color(red = 0x00, green = 0x71, blue = 0xBC)

    val email = remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
            .padding(20.dp)
    ){

        Row(horizontalArrangement = Arrangement.Start){
            Image(
                painter = rememberVectorPainter(image = Icons.Filled.ArrowBack),
                contentDescription = "Назад",
                modifier = Modifier.clickable { navController.popBackStack() }
            )
        }
        Image(
            painter = painterResource(id = R.drawable.icon),
            contentDescription = "App's Icon",
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Forgot Password?",
            color = btnBackColor,
            fontSize = 48.sp,
            modifier = Modifier.padding(top = 12.dp)
        )

        Text(
            text = "Don't worry! It happens. Please enter the address associated with your account.",
            modifier = Modifier.padding(top = 12.dp),
            color = Color.Gray
        )

        TextField(
            value = email.value,
            onValueChange = { it ->
                email.value = it
            },
            placeholder = { Text(text = "Email") },
            keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Email ),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(top = 12.dp)
                .clickable { /*TODO: Сделать валидацию для поля ввода Email*/},
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.LightGray,
                unfocusedIndicatorColor = Color.LightGray
            ),
            leadingIcon = { Icon(imageVector = Icons.Filled.Email, contentDescription = "Email icon") }
        )
        Button(
            onClick = { /*TODO: Сделать обработку данных для востановления пароля*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = btnBackColor
            )
        ) {
            Text( text = "Submit", color = Color.White, modifier = Modifier.padding(6.dp))
        }
    }
}
package com.example.sky.android.screens

import com.example.sky.android.R
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sky.navigation.NavRoute

@Composable
fun LoginScreen(navController: NavHostController) {

    val btnBackColor = Color(red = 0x00, green = 0x71, blue = 0xBC)
    val linkColor = Color(red = 0x00, green = 0x52, blue = 0xcc)

    val isHidePass = remember{ mutableStateOf(true) }
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
            .padding(20.dp)
    ){
        Image(
            painter = painterResource(id = R.drawable.icon),
            contentDescription = "App's Icon",
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Login",
            color = btnBackColor,
            fontSize = 48.sp,
            modifier = Modifier.padding(top = 12.dp)
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
                .clickable { /*TODO: Сделать валидацию для поля ввода Email*/ },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.LightGray,
                unfocusedIndicatorColor = Color.LightGray
            ),
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email icon") }
        )
        TextField(
            value = password.value,
            onValueChange = { it ->
                password.value = it
            },
            placeholder = { Text("Password") },
            keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Password ),
            visualTransformation = if (isHidePass.value) PasswordVisualTransformation() else VisualTransformation.None,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(top = 12.dp)
                .clickable { /*TODO: Сделать валидацию для поля ввода Password*/ },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.LightGray,
                unfocusedIndicatorColor = Color.LightGray
            ),
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password icon") },
            trailingIcon = { Icon(
                imageVector = ImageVector.vectorResource(id = if (isHidePass.value) R.drawable.eye_hide else R.drawable.eye_show),
                contentDescription = "Password Hide icon",
                modifier = Modifier.clickable(onClick = {
                    isHidePass.value = !isHidePass.value
                })
            )
            },
        )
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Forgot password?",
                modifier = Modifier
                    .padding(top = 6.dp)
                    .clickable { navController.navigate(route = NavRoute.ForgotPassword.route) },
                color = linkColor
            )
        }
        Button(
            onClick = { /*TODO: Сделать обработку данных для входа*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = btnBackColor
            )
        ) {
            Text( text = "Login", color = Color.White, modifier = Modifier.padding(6.dp))
        }
        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 6.dp)
        ){
            Text(text = "New to Logistics? ")
            Text(
                text = " Register",
                color = linkColor,
                modifier = Modifier.clickable { navController.navigate(route = NavRoute.SignUp.route) }
            )
        }
    }
}
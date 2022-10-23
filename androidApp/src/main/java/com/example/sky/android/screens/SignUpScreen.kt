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
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
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
fun SignUpScreen(navController: NavHostController) {
    val btnBackColor = Color(red = 0x00, green = 0x71, blue = 0xBC)
    val linkColor = Color(red = 0x00, green = 0x52, blue = 0xcc)

    val isHidePass_1 = remember{ mutableStateOf(true) }
    val isHidePass_2 = remember{ mutableStateOf(true) }
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val password_1 = remember { mutableStateOf(TextFieldValue("")) }
    val password_2 = remember { mutableStateOf(TextFieldValue("")) }
    val nickname = remember { mutableStateOf(TextFieldValue("")) }

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
            text = "Sign Up",
            color = btnBackColor,
            fontSize = 48.sp,
            modifier = Modifier.padding(top = 12.dp)
        )
        TextField(
            value = nickname.value,
            onValueChange = { it ->
                nickname.value = it
            },
            placeholder = { Text("Nickname") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(top = 12.dp)
                .clickable { /*TODO: Сделать валидацию для поля ввода nickname*/ },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.LightGray,
                unfocusedIndicatorColor = Color.LightGray
            ),
            leadingIcon = { Icon(Icons.Filled.Face, contentDescription = "Nickname icon") },
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
            value = password_1.value,
            onValueChange = { it ->
                password_1.value = it
            },
            placeholder = { Text("Password") },
            keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Password ),
            visualTransformation = if (isHidePass_1.value) PasswordVisualTransformation() else VisualTransformation.None,
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
                imageVector = ImageVector.vectorResource(id = if (isHidePass_1.value) R.drawable.eye_hide else R.drawable.eye_show),
                contentDescription = "Password Hide icon",
                modifier = Modifier.clickable(onClick = {
                    isHidePass_1.value = !isHidePass_1.value
                })
            )
            },
        )
        TextField(
            value = password_2.value,
            onValueChange = { it ->
                password_2.value = it
            },
            placeholder = { Text("Repeat password") },
            keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Password ),
            visualTransformation = if (isHidePass_2.value) PasswordVisualTransformation() else VisualTransformation.None,
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
                imageVector = ImageVector.vectorResource(id = if (isHidePass_2.value) R.drawable.eye_hide else R.drawable.eye_show),
                contentDescription = "Password Hide icon",
                modifier = Modifier.clickable(onClick = {
                    isHidePass_2.value = !isHidePass_2.value
                })
            )
            },
        )
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(top = 6.dp).fillMaxWidth()
        ) {
            Text(text = "By signing up, you're agree to our ", color = Color.Gray)
            Text(
                text = "Terms & Conditions ",
                modifier = Modifier
                    .clickable { /*TODO: Сделать переход из экрана Регистрации на экран Terms & Conditions*/ },
                color = linkColor
            )
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ){
            Text(text = "and ", color = Color.Gray)
            Text(
                text = "Privacy Policy ",
                modifier = Modifier
                    .clickable { /*TODO: Сделать переход из экрана Регистрации на экран Privacy Policy*/ },
                color = linkColor
            )
        }
        Button(
            onClick = { /*TODO: Сделать продолжене регистрации*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = btnBackColor
            )
        ) {
            Text( text = "Continue", color = Color.White, modifier = Modifier.padding(6.dp))
        }
        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(top = 6.dp).fillMaxWidth()
        ){
            Text(text = "Joined us before? ", color = Color.Gray)
            Text(
                text = " Login",
                color = linkColor,
                modifier = Modifier.clickable { navController.navigate(route = NavRoute.Login.route) }
            )
        }
    }
}
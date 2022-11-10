package com.example.sky.android.screens

import android.util.Log
import android.util.Patterns
import com.example.sky.android.R
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
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
import com.example.sky.ui.theme.linkColor
import com.example.sky.ui.theme.mainColor
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

fun consistDigits(str: String): Boolean {
    for (i in 0..str.length-1)
        if (str[i].isDigit())
            return true
    return false
}

//@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun SignUpScreen(navController: NavHostController) {
    val auth = Firebase.auth

    val nickname = remember { mutableStateOf(TextFieldValue("")) }
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val isHidePass_1 = remember{ mutableStateOf(true) }
    val isHidePass_2 = remember{ mutableStateOf(true) }
    val password_1 = remember { mutableStateOf(TextFieldValue("")) }
    val password_2 = remember { mutableStateOf(TextFieldValue("")) }
    val phone = remember { mutableStateOf("") }

    val isEmailValid by derivedStateOf { Patterns.EMAIL_ADDRESS.matcher(email.value.text).matches() }
    val isPassword1Valid by derivedStateOf { password_1.value.text.length > 7 }
    val isPassword2Valid by derivedStateOf { password_2.value.text.length > 7 && password_1.value.text.equals(password_2.value.text)}
    val isNickNameValid by derivedStateOf { nickname.value.text.length >= 4 && nickname.value.text.length <= 16 && !consistDigits(nickname.value.text) }
    val isPhoneValid by derivedStateOf { Patterns.PHONE.matcher(phone.value).matches() }

    var showErrorMessages by remember {
        mutableStateOf(false)
    }
    val openDialog = remember { mutableStateOf(false) }

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
            color = mainColor,
            fontSize = 48.sp,
            modifier = Modifier.padding(top = 12.dp)
        )

        // Никнейм
        Column(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = nickname.value,
                onValueChange = { it ->
                    nickname.value = it
                },
                placeholder = { Text("Nickname") },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(top = 12.dp),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.LightGray
                ),
                leadingIcon = { Icon(Icons.Filled.Face, contentDescription = "Nickname icon") },
                isError = showErrorMessages && !isNickNameValid,
            )

            if (showErrorMessages && !isNickNameValid)
                Text(
                    text = if (nickname.value.text.length < 4) "Little nickname" else if (nickname.value.text.length > 16) "Large nickname" else "The nickname must not contain numbers",
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth()
                )
        }

        // Телефон
        Column(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = phone.value,
                onValueChange = { it ->
                    phone.value = it
                },
                placeholder = { Text(text = "Phone") },
                keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Email ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(top = 12.dp),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.LightGray
                ),
                leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = "Email icon") },
                isError = showErrorMessages && !isPhoneValid,
            )

            if (showErrorMessages && ! isEmailValid)
                Text(
                    text = "Incorrectly email",
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth()
                )
        }

        // Почта
        Column(modifier = Modifier.fillMaxWidth()) {
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
                    .padding(top = 12.dp),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.LightGray
                ),
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email icon") },
                isError = showErrorMessages && !isEmailValid,
            )

            if (showErrorMessages && ! isEmailValid)
                Text(
                    text = "Incorrectly email",
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth()
                )
        }

        // Пароль 1
        Column(modifier = Modifier.fillMaxWidth()) {
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
                    .padding(top = 12.dp),
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
                isError = showErrorMessages && !isPassword1Valid,
            )

            if (showErrorMessages && ! isPassword1Valid)
                Text(
                    text = "Incorrectly password",
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth()
                )
        }

        // Пароль 2
        Column(modifier = Modifier.fillMaxWidth()) {
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
                    .padding(top = 12.dp),
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
                    }))},
                isError = showErrorMessages && !isPassword2Valid,
            )

            if (showErrorMessages && ! isPassword2Valid)
                Text(
                    text = if (password_1.value.text.equals(password_2.value.text)) "Incorrectly password" else "Passwords are not similar",
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth()
                )
        }

        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth()
        ) {
            Text(text = "By signing up, you're agree to our ", color = Color.Gray)
            Text(
                text = "Terms & Conditions ",
                modifier = Modifier
                    .clickable { navController.navigate(route = NavRoute.TermsAndConditions.route) },
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
                    .clickable { navController.navigate(route = NavRoute.PrivacyPolicy.route) },
                color = linkColor
            )
        }
        Button(
            onClick = {
                if (!isEmailValid || !isNickNameValid || !isPassword1Valid || !isPassword2Valid || !isPhoneValid)
                    showErrorMessages = true
                else {
                    showErrorMessages = false
                    auth.createUserWithEmailAndPassword(
                        email.value.text,
                        password_1.value.text
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.i("SignUpAuthorization", "SignUp is Complete and successful")
                            navController.navigate(NavRoute.Main.route)
                        } else {
                            Log.e("SignUpAuthorization", "SignUp is Complete and not successful", it.exception)
                            openDialog.value = true
                        }
                    }.addOnCanceledListener {
                        Log.i("SignUpAuthorization", "SignUp is Cancel")
                    }.addOnFailureListener {
                        Log.e("SignUpAuthorization", "SignUp is Fail and failed", it)
                    }
                    /*TODO: Сделать продолжене регистрации*/
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = mainColor
            )
        ) {
            Text( text = "Continue", color = Color.White, modifier = Modifier.padding(6.dp))
        }
        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth()
        ){
            Text(text = "Joined us before? ", color = Color.Gray)
            Text(
                text = " Login",
                color = linkColor,
                modifier = Modifier.clickable { navController.navigate(route = NavRoute.Login.route) }
            )
        }
    }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = {
                Text(
                    text = "Error",
                    color = Color.Red
                )
            },
            text = {
                Column() {
                    Text(
                        text  = "A user with the same email already exists"
                    )
                }
            },
            buttons = {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { openDialog.value = false },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = mainColor
                        ),

                        ) {
                        Text( text = "Okay", color = Color.White, modifier = Modifier.padding(6.dp))
                    }
                }
            }
        )
    }
}

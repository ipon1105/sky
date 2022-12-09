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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sky.navigation.NavRoute
import com.example.sky.ui.theme.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

@Composable
fun LoginScreen(navController: NavHostController) {
    val db = FirebaseFirestore.getInstance()
    db.collection("user")
        .get()
        .addOnCompleteListener{
            Log.d("Firestore", "complete")
        }
        .addOnCanceledListener {
            Log.d("Firestore", "canceled")
        }
        .addOnFailureListener{
            Log.d("Firestore", "fail")
        }
        .addOnSuccessListener{
            Log.d("Firestore", "success")
        }
    //

    //TODO: Сделать проверку интернета
    val auth = Firebase.auth

    var isHidePass by remember{ mutableStateOf(true) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    val isEmailValid by derivedStateOf { Patterns.EMAIL_ADDRESS.matcher(email.text).matches() }
    val isPasswordValid by derivedStateOf { password.text.length > 7 }
    var openDialog by remember { mutableStateOf(false) }
    var showErrorMessages by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
            .padding(ScreenArea)
    ){
        //Кнопка назад
        Row(horizontalArrangement = Arrangement.Start){
            Image(
                painter = rememberVectorPainter(image = Icons.Filled.ArrowBack),
                contentDescription = stringResource(id = R.string.imageDescriptionBack),
                colorFilter = ColorFilter.tint(Color.Transparent)
            )
        }

        // Изображение главной иконки
        Image(
            painter = painterResource(id = R.drawable.icon),
            contentDescription = stringResource(id = R.string.iconDescription),
            modifier = Modifier.fillMaxWidth()
        )

        // Заголовок страницы
        Text(
            text = stringResource(id = R.string.login),
            color = mainColor,
            fontSize = VeryLargeFont,
            modifier = Modifier.padding(top = ComponentDiffNormal)
        )

        // Логин
        Column(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text(text = stringResource(id = R.string.email)) },
                keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Email ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(top = ComponentDiffNormal),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.LightGray
                ),
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = stringResource(id = R.string.imageDescriptionEmail)) },
                isError = !isEmailValid && showErrorMessages,
            )

            if (showErrorMessages && !isEmailValid)
                Text(
                    text = stringResource(id = R.string.emailError),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = ErrorStart)
                        .fillMaxWidth()
                )
        }

        // Пароль
        Column(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text(text = stringResource(id = R.string.password)) },
                keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Password ),
                visualTransformation = if (isHidePass) PasswordVisualTransformation() else VisualTransformation.None,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(top = ComponentDiffNormal),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.LightGray
                ),
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = stringResource(id = R.string.imageDescriptionPassword)) },
                trailingIcon = { Icon(
                    imageVector = ImageVector.vectorResource(id = if (isHidePass) R.drawable.eye_hide else R.drawable.eye_show),
                    contentDescription = stringResource(id = R.string.imageDescriptionHideShowPassword),
                    modifier = Modifier.clickable(onClick = { isHidePass = !isHidePass })
                )},
                isError = !isPasswordValid && showErrorMessages
            )

            if (showErrorMessages && !isPasswordValid)
                Text(
                    text = stringResource(id = R.string.passwordError),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = ErrorStart)
                        .fillMaxWidth()
                )
        }

        // Ссылка на страницу забыл пароль
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.forgotPassword),
                modifier = Modifier
                    .padding(top = TextTopSmall)
                    .clickable { navController.navigate(route = NavRoute.ForgotPassword.route) },
                color = linkColor
            )
        }

        // Кнопка входа
        Button(
            onClick = {
                //navController.navigate(NavRoute.Main.route)
                if (isEmailValid && isPasswordValid){
                    auth.signInWithEmailAndPassword(
                        email.text,
                        password.text
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.i("LoginAuthorization", "Log is Complete and successful")
                            navController.navigate(NavRoute.Main.route)
                        } else {
                            Log.e("LoginAuthorization", "Log is Complete and not successful", it.exception)
                            openDialog = true
                        }
                    }.addOnCanceledListener {
                        Log.i("LoginAuthorization", "Log is Cancel")
                    }.addOnFailureListener {
                        Log.e("LoginAuthorization", "Log is Fail and failed", it)
                    }
                    showErrorMessages = false
                }
                else
                    showErrorMessages = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = ComponentDiffLarge),
            shape = RoundedCornerShape(size = largeShape),
            colors = ButtonDefaults.buttonColors( backgroundColor = mainColor),
        ) {
            Text( text = stringResource(id = R.string.login), color = Color.White, modifier = Modifier.padding(ButtonArea))
        }

        // Ссылка на страницу регистрации
        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = ComponentDiffSmall)
        ){
            Text(text = stringResource(id = R.string.loginMsgRegister) + " ", color = Color.Gray)
            Text(text = stringResource(id = R.string.loginMsgRegisterLink),
                 color = linkColor,
                 modifier = Modifier.clickable { navController.navigate(route = NavRoute.SignUp.route) }
            )
        }
    }

    // Диалоговое окно
    if (openDialog) {
        AlertDialog(
            onDismissRequest = { openDialog = false },
            title = { Text(text = stringResource(id = R.string.error), color = Color.Red) },
            text = { Text( text  = stringResource(id = R.string.loginErrorMsg) ) },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = alertDialogArea)
                        .padding(top = 0.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { openDialog = false },
                        shape = RoundedCornerShape(largeShape),
                        colors = ButtonDefaults.buttonColors( backgroundColor = mainColor ),
                    ) {
                        Text( text = stringResource(id = R.string.alertOkay), color = Color.White, modifier = Modifier.padding(all = TextTopSmall))
                    }
                }
            }
        )
    }
}
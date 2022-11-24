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
import com.google.firebase.ktx.Firebase

fun consistDigits(str: String): Boolean {
    //for (i in 0..str.length-1)
    for (i in 0 until str.length)
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
    val isHidePass = remember{ mutableStateOf(true) }
    val isHideProvingPass = remember{ mutableStateOf(true) }
    val password = remember { mutableStateOf(TextFieldValue("")) }
    val provingPassword = remember { mutableStateOf(TextFieldValue("")) }
    val phone = remember { mutableStateOf("") }

    val isEmailValid by derivedStateOf { Patterns.EMAIL_ADDRESS.matcher(email.value.text).matches() }
    val isHidePassValid by derivedStateOf { password.value.text.length > 7 }
    val isHideProvingPassValid by derivedStateOf { provingPassword.value.text.length > 7 && password.value.text.equals(provingPassword.value.text)}
    val isNickNameValid by derivedStateOf { nickname.value.text.length >= 4 && nickname.value.text.length <= 16 && !consistDigits(nickname.value.text) }
    val isPhoneValid by derivedStateOf { Patterns.PHONE.matcher(phone.value).matches() }

    var showErrorMessages by remember { mutableStateOf(false) }
    val openDialog = remember { mutableStateOf(false) }

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
                modifier = Modifier.clickable { navController.popBackStack() }
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
            text = stringResource(id = R.string.signUp),
            color = mainColor,
            fontSize = largeFont,
            modifier = Modifier.padding(top = ComponentDiffNormal)
        )

        // Никнейм
        Column(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = nickname.value,
                onValueChange = { it -> nickname.value = it },
                placeholder = { Text(stringResource(id = R.string.nickname)) },
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
                leadingIcon = { Icon(Icons.Filled.Face, contentDescription = stringResource(id = R.string.imageDescriptionNickname)) },
                isError = showErrorMessages && !isNickNameValid,
            )

            if (showErrorMessages && !isNickNameValid)
                Text(
                    text = stringResource(id = if (nickname.value.text.length < 4) R.string.signUpNicknameLittleError else if (nickname.value.text.length > 16) R.string.signUpNicknameLargeError else R.string.signUpNicknameNumberError),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = ErrorStart)
                        .fillMaxWidth()
                )
        }

        // Телефон
        Column(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = phone.value,
                onValueChange = { it -> phone.value = it },
                placeholder = { Text(text = stringResource(id = R.string.phone)) },
                keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Phone ),
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
                leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = stringResource(id = R.string.imageDescriptionPhone)) },
                isError = showErrorMessages && !isPhoneValid,
            )

            if (showErrorMessages && ! isPhoneValid)
                Text(
                    text = stringResource(id = R.string.phoneError),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = ErrorStart)
                        .fillMaxWidth()
                )
        }

        // Почта
        Column(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = email.value,
                onValueChange = { it -> email.value = it },
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
                isError = showErrorMessages && !isEmailValid,
            )

            if (showErrorMessages && ! isEmailValid)
                Text(
                    text = stringResource(id = R.string.emailError),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = ErrorStart)
                        .fillMaxWidth()
                )
        }

        // Пароль 1
        Column(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = password.value,
                onValueChange = { it -> password.value = it },
                placeholder = { Text(stringResource(id = R.string.password)) },
                keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Password ),
                visualTransformation = if (isHidePass.value) PasswordVisualTransformation() else VisualTransformation.None,
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
                    imageVector = ImageVector.vectorResource(id = if (isHidePass.value) R.drawable.eye_hide else R.drawable.eye_show),
                    contentDescription = stringResource(id = R.string.imageDescriptionHideShowPassword),
                    modifier = Modifier.clickable(onClick = { isHidePass.value = !isHidePass.value }),
                )
                },
                isError = showErrorMessages && !isHidePassValid,
            )

            if (showErrorMessages && ! isHidePassValid)
                Text(
                    text = stringResource(id = R.string.passwordError),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = ErrorStart)
                        .fillMaxWidth(),
                )
        }

        // Пароль 2
        Column(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = provingPassword.value,
                onValueChange = { it -> provingPassword.value = it },
                placeholder = { Text(stringResource(id = R.string.signUpRepeatPassword)) },
                keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Password ),
                visualTransformation = if (isHideProvingPass.value) PasswordVisualTransformation() else VisualTransformation.None,
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
                    imageVector = ImageVector.vectorResource(id = if (isHideProvingPass.value) R.drawable.eye_hide else R.drawable.eye_show),
                    contentDescription = stringResource(id = R.string.imageDescriptionHideShowPassword),
                    modifier = Modifier.clickable(onClick = { isHideProvingPass.value = !isHideProvingPass.value })
                )},
                isError = showErrorMessages && !isHideProvingPassValid,
            )

            if (showErrorMessages && ! isHideProvingPassValid)
                Text(
                    text = stringResource( id = if (password.value.text.equals(provingPassword.value.text)) R.string.passwordError else R.string.signUpPasswordNotSimilarError),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = ErrorStart)
                        .fillMaxWidth()
                )
        }

        // Ссылки на Пользовательсткое соглашение и Политику конфиденциальности
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .padding(top = TextTopSmall)
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.signUpAgree) + " ", color = Color.Gray)
                Text(text = stringResource(id = R.string.signUpTC),
                     modifier = Modifier.clickable { navController.navigate(route = NavRoute.TermsAndConditions.route) },
                     color = linkColor
                )
            }
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(text = stringResource(id = R.string.signUpAnd) + " ", color = Color.Gray)
                Text(text = stringResource(id = R.string.signUpPrivacyPolicy),
                     modifier = Modifier.clickable { navController.navigate(route = NavRoute.PrivacyPolicy.route) },
                     color = linkColor
                )
            }
        }

        // Кнопка продолжить
        Button(
            onClick = {
                if (!isEmailValid || !isNickNameValid || !isHidePassValid || !isHideProvingPassValid || !isPhoneValid)
                    showErrorMessages = true
                else {
                    showErrorMessages = false
                    auth.createUserWithEmailAndPassword(
                        email.value.text,
                        password.value.text
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
                .padding(top = ComponentDiffLarge),
            shape = RoundedCornerShape(largeShape),
            colors = ButtonDefaults.buttonColors(backgroundColor = mainColor)
        ) {
            Text( text = stringResource(id = R.string.signUpContinue), color = Color.White, modifier = Modifier.padding(ButtonArea))
        }

        // Ссылка на страницу входа
        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(top = TextTopSmall)
                .fillMaxWidth()
        ){
            Text(text = stringResource(id = R.string.signUpJoin)+" ", color = Color.Gray)
            Text(text = stringResource(id = R.string.signUpLogin),
                 color = linkColor,
                 modifier = Modifier.clickable { navController.navigate(route = NavRoute.Login.route) }
            )
        }
    }

    // Диалоговое окно
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text( text = stringResource(id = R.string.error), color = Color.Red) },
            text = { Text(text  = stringResource(id = R.string.signUpEmailExists)) },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = alertDialogArea)
                        .padding(top = 0.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { openDialog.value = false },
                        shape = RoundedCornerShape(size = largeShape),
                        colors = ButtonDefaults.buttonColors( backgroundColor = mainColor),
                    ) {
                        Text( text = stringResource(id = R.string.alertOkay), color = Color.White, modifier = Modifier.padding(all = TextTopSmall))
                    }
                }
            }
        )
    }
}
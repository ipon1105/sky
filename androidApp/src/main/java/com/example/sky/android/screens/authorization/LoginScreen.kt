package com.example.sky.android.screens

import android.annotation.SuppressLint
import com.example.sky.android.R
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.sky.android.composables.EmailTextField
import com.example.sky.android.composables.NoInternet
import com.example.sky.android.composables.PasswordTextField
import com.example.sky.android.models.authorization.LoginViewModel
import com.example.sky.android.utils.connection.ConnectionState
import com.example.sky.android.utils.connection.connectivityState
import com.example.sky.ui.theme.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun LoginScreen(navController: NavHostController) {
    val viewModel = viewModel<LoginViewModel>()

    // Интернет
    val connection by connectivityState()
    viewModel.setInternet(connection === ConnectionState.Available)

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
        EmailTextField(
            email = viewModel.email,
            valid = viewModel.isEmailValid,
            isErrorShow = viewModel.showErrorMessages,
            onValueChange = { viewModel.setEmail(it) },
        )

        // Пароль
        PasswordTextField(
            password = viewModel.password,
            valid = viewModel.isPasswordValid,
            isErrorShow = viewModel.showErrorMessages,
            onValueChange = { viewModel.setPassword(it) },
        )

        // Ссылка на страницу забыл пароль
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.forgotPassword),
                modifier = Modifier
                    .padding(top = TextTopSmall)
                    .clickable { viewModel.goLinkToForgotPassword(navController = navController) },
                color = linkColor
            )
        }

        // Кнопка входа
        Button(
            enabled = !viewModel.isAuthLoading,
            onClick = { viewModel.btnLoginClick(navController = navController) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = ComponentDiffLarge),
            shape = RoundedCornerShape(size = largeShape),
            colors = ButtonDefaults.buttonColors( backgroundColor = mainColor),
        ) {
            if (viewModel.isAuthLoading)
                CircularProgressIndicator(color = mainColor)
            else
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
                 modifier = Modifier.clickable { viewModel.goLinkToRegister(navController = navController) }
            )
        }
    }

    // Диалоговое окно
    if (viewModel.showDialog)
        AlertDialog(
            onDismissRequest = { viewModel.setShowDialog(false) },
            title = { Text(text = stringResource(id = R.string.error), color = Color.Red) },
            text = { Text( text = viewModel.dialogMsg ) },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = alertDialogArea)
                        .padding(top = 0.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { viewModel.setShowDialog(false) },
                        shape = RoundedCornerShape(largeShape),
                        colors = ButtonDefaults.buttonColors( backgroundColor = mainColor ),
                    ) {
                        Text( text = stringResource(id = R.string.alertOkay), color = Color.White, modifier = Modifier.padding(all = TextTopSmall))
                    }
                }
            }
        )

    // Сообщение об отсутствие интернета
    if (viewModel.showDialog && !viewModel.internet)
        NoInternet(onDismissRequest = {viewModel.setShowDialog(false)}, btnOnClick = {viewModel.setShowDialog(false)})
}
package com.example.sky.android.screens

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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavHostController
import com.example.sky.ui.theme.*

@Composable
fun ForgotPasswordScreen(navController: NavHostController) {

    val email = remember { mutableStateOf(TextFieldValue("")) }
    val isEmailValid = derivedStateOf { Patterns.EMAIL_ADDRESS.matcher(email.value.text).matches() }
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
            text = stringResource(id = R.string.forgotPassword),
            color = mainColor,
            fontSize = largeFont,
            modifier = Modifier.padding(top = ComponentDiffNormal)
        )

        // Вспомогательный текст под заголовком
        Text(
            text = stringResource(id = R.string.forgotPasswordMsg),
            modifier = Modifier.padding(top = ComponentDiffNormal),
            color = Color.Gray
        )

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
                leadingIcon = { Icon(imageVector = Icons.Filled.Email, contentDescription = stringResource(id = R.string.imageDescriptionEmail)) },
                isError = !isEmailValid.value && showErrorMessages,
            )

            if (showErrorMessages && !isEmailValid.value)
                Text(
                    text = stringResource(id = R.string.emailError),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = ErrorStart)
                        .fillMaxWidth()
                )
        }

        // Кнопка подтвердить
        Button(
            onClick = {
                if (!isEmailValid.value)
                    showErrorMessages = true
                else {
                    showErrorMessages = false
                    /*TODO: Сделать обработку данных для востановления пароля*/
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = ComponentDiffLarge),
            shape = RoundedCornerShape(size = largeShape),
            colors = ButtonDefaults.buttonColors( backgroundColor = mainColor ),
        ) {
            Text(text = stringResource(id = R.string.forgotPasswordSubmit), color = Color.White, modifier = Modifier.padding(all = ButtonArea))
        }
    }
}
package com.example.sky.android.screens

import android.annotation.SuppressLint
import com.example.sky.android.R
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.sky.android.composables.*
import com.example.sky.android.models.authorization.SignUpViewModel
import com.example.sky.android.utils.connection.ConnectionState
import com.example.sky.android.utils.connection.connectivityState
import com.example.sky.ui.theme.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

fun consistDigits(str: String): Boolean {
    for (i in 0 until str.length)
        if (str[i].isDigit())
            return true
    return false
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalCoroutinesApi::class)
@SuppressLint("UnrememberedMutableState", "StateFlowValueCalledInComposition")
@Composable
fun SignUpScreen(navController: NavHostController) {
    val viewModel = viewModel<SignUpViewModel>()

    // Интернет
    val connection by connectivityState()
    viewModel.setInternet( connection === ConnectionState.Available )

    // Строка аннотации
    val annotatedLinkString: AnnotatedString = buildAnnotatedString {

        val PP = stringResource(id = R.string.signUpPrivacyPolicy, )
        val TC = stringResource(id = R.string.signUpTC, )
        val res = stringResource(id = R.string.signUpAgree2, PP, TC)

        val startPP = res.indexOf(PP)
        val startTC = res.indexOf(TC)

        val endPP = startPP + PP.length
        val endTC = startTC + TC.length

        append(res)

        // Обычный текст
        addStyle(
            style = SpanStyle(
                color = Color.Gray,
                fontSize = SmallFont,
            ), start = 0, end = res.length
        )

        // Ссылка
        addStyle(
            style = SpanStyle(
                color = linkColor,
                fontSize = SmallFont,
            ), start = startPP, end = endPP
        )

        // Ссылка
        addStyle(
            style = SpanStyle(
                color = linkColor,
                fontSize = SmallFont,
            ), start = startTC, end = endTC
        )

        // Для перехода на политику конфиденциальности
        addStringAnnotation(
            tag = "navigation",
            annotation = "PrivacyPolicy",
            start = startPP,
            end = endPP
        )

        // Для перехода на пользовательское соглашение
        addStringAnnotation(
            tag = "navigation",
            annotation = "TermsAndConditions",
            start = startTC,
            end = endTC
        )
    }

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
                modifier = Modifier.clickable { viewModel.goToBack(navController) }
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
            fontSize = VeryLargeFont,
            modifier = Modifier.padding(top = ComponentDiffNormal)
        )

        // Имя
        NameTextFiled(
            name = viewModel.name,
            valid = viewModel.isNameValid,
            isErrorShow = viewModel.showErrorMessages,
            onValueChange = { viewModel.setName(it) },
        )

        // Фамилия
        SurnameTextFiled(
            surname = viewModel.surname,
            valid = viewModel.isSurnameValid,
            isErrorShow = viewModel.showErrorMessages,
            onValueChange = { viewModel.setSurname(it) },
        )

        // Отчество
        PatronymicTextFiled(
            patronymic = viewModel.patronymic,
            valid = viewModel.isPatronymicValid,
            isErrorShow = viewModel.showErrorMessages,
            onValueChange = { viewModel.setPatronymic(it) },
        )

        // Телефон
        TelephoneTextField(
            telephone = viewModel.phone,
            valid = viewModel.isPhoneValid,
            isErrorShow = viewModel.showErrorMessages,
            onValueChange = { viewModel.setPhone(it) }
        )

        // Почта
        EmailTextField(
            email = viewModel.email,
            valid = viewModel.isEmailValid,
            isErrorShow = viewModel.showErrorMessages,
            onValueChange = { viewModel.setEmail(it) },
        )

        // Пароль 1
        PasswordTextField(
            password = viewModel.password,
            valid = viewModel.isPassValid,
            isErrorShow = viewModel.showErrorMessages,
            onValueChange = { viewModel.setPassword(it) },
        )

        // Пароль 2
        PasswordTextField(
            password = viewModel.provingPassword,
            valid = viewModel.isProvingPassValid,
            isErrorShow = viewModel.showErrorMessages,
            onValueChange = { viewModel.setProvingPassword(it) },
            error = stringResource( id = if (viewModel.password.equals(viewModel.provingPassword)) R.string.passwordError else R.string.signUpPasswordNotSimilarError),
            placeholder = stringResource(id = R.string.signUpRepeatPassword)
        )

        // Контейрнер групирующий радио кнопки
        Column(modifier = Modifier
            .padding(top = ComponentDiffNormal, start = ComponentDiffNormal)
            .selectableGroup()
        ) {
            // Контейнр компонента
            Row(modifier = Modifier.clickable { viewModel.setStatus(1) }) {
                // Светлая тема
                CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false){
                    RadioButton(
                        selected = viewModel.status == 1,
                        colors = RadioButtonDefaults.colors(
                            unselectedColor = DarkGray,
                            selectedColor = HeaderMainColorMain,
                        ),
                        onClick = { viewModel.setStatus(1) },
                    )
                }

                // Текс подсказка
                Text(
                    text = stringResource(id = R.string.workerSelect),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = ComponentDiffNormal),
                    color = GrayTextColor
                )
            }

            // Контейнр компонента
            Row(modifier = Modifier
                .padding(top = ComponentDiffSmall)
                .clickable { viewModel.setStatus(2) }) {
                // Тёмная тема
                CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false){
                    RadioButton(
                        selected = viewModel.status == 2,
                        onClick = { viewModel.setStatus(2) },
                        colors = RadioButtonDefaults.colors(
                            unselectedColor = DarkGray,
                            selectedColor = HeaderMainColorMain,
                        )
                    )
                }

                // Текс подсказка
                Text(
                    text = stringResource(id = R.string.administratorSelect),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = ComponentDiffNormal),
                    color = GrayTextColor
                )
            }
        }

        // Ссылки на Пользовательсткое соглашение и Политику конфиденциальности
        ClickableText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = ComponentDiffSmall),
            text = annotatedLinkString,
            onClick = {
                annotatedLinkString
                    .getStringAnnotations("navigation", it, it)
                    .firstOrNull()?.let { stringAnnotation ->
                        when(stringAnnotation.item){
                            "PrivacyPolicy" -> viewModel.goLinkToPrivacyPolicy(navController)
                            "TermsAndConditions" -> viewModel.goLinkToTC(navController)
                        }
                    }
            }
        )

        // Кнопка продолжить
        Button(
            enabled = !viewModel.isAuthLoading,
            onClick = { viewModel.btnContinueClick(navController) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = ComponentDiffLarge),
            shape = RoundedCornerShape(largeShape),
            colors = ButtonDefaults.buttonColors(backgroundColor = mainColor)
        ) {
            if (viewModel.isAuthLoading)
                CircularProgressIndicator(color = mainColor)
            else
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
                 modifier = Modifier.clickable { viewModel.goLinkToLogin(navController)}
            )
        }
    }

    // Диалоговое окно
    if (viewModel.showDialog && viewModel.internet)
        AlertDialog(
            onDismissRequest = { viewModel.setShowDialog(false) },
            title = { Text( text = stringResource(id = R.string.error), color = FlatRed) },
            text =  { Text( text = viewModel.dialogMsg) },
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
                        shape = RoundedCornerShape(size = largeShape),
                        colors = ButtonDefaults.buttonColors( backgroundColor = mainColor),
                    ) {
                        Text( text = stringResource(id = R.string.alertOkay), color = Color.White, modifier = Modifier.padding(all = TextTopSmall))
                    }
                }
            }
        )

    // Сообщение об отсутствие интернета
    if (viewModel.showDialog && !viewModel.internet)
        NoInternet(onDismissRequest = {viewModel.setShowDialog(false)}, btnOnClick = {viewModel.setShowDialog(false)})

    // Всплывающие сообщения
    if (viewModel.isTCError || viewModel.isPrivacyPolicyError || viewModel.isLoginError)
        mToast(LocalContext.current, stringResource(id = R.string.signUpGoLinkError))
    if (viewModel.isBackError)
        mToast(LocalContext.current, stringResource(id = R.string.signUpGoBackError))
}
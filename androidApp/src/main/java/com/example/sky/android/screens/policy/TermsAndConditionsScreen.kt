package com.example.sky.android.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.sky.android.R
import com.example.sky.ui.theme.ComponentDiffNormal
import com.example.sky.ui.theme.ScreenArea
import com.example.sky.ui.theme.largeFont
import com.example.sky.ui.theme.mainColor

@Composable
fun TermsAndConditionsScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
            .padding(ScreenArea)
    ) {
        // Кнопка назад
        Row(horizontalArrangement = Arrangement.Start) {
            Image(
                painter = rememberVectorPainter(image = Icons.Filled.ArrowBack),
                contentDescription = stringResource(id = R.string.imageDescriptionBack),
                modifier = Modifier.clickable { navController.popBackStack() }
            )
        }

        // Заголовок страницы
        Text(
            text = stringResource(id = R.string.termsAndConditions),
            color = mainColor,
            fontSize = largeFont,
            modifier = Modifier.padding(top = ComponentDiffNormal)
        )

        /*TODO: Добавить текст для Terms & Conditions*/
        Text(
            text = stringResource(id = R.string.termsAndConditionText),
            color = Color.Gray,
            modifier = Modifier.padding(top = ComponentDiffNormal)
        )
    }
}
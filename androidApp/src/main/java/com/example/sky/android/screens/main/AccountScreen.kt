package com.example.sky.android.screens

import com.example.sky.android.R
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sky.navigation.NavRoute
import com.example.sky.ui.theme.*

@Composable
fun AccountScreen(navController: NavHostController) {

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        backgroundColor = HeaderMainColorMain,
        drawerShape =  RoundedCornerShape(
            bottomStart = topBarCornerShape,
            bottomEnd = topBarCornerShape
        ),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(
                            bottomEnd = topBarCornerShape,
                            bottomStart = topBarCornerShape
                        )
                    ),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                // Кнопка назад
                Image(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.imageDescriptionBack),
                    modifier = Modifier
                        .padding(all = ComponentDiffNormal)
                        .clickable { navController.popBackStack() },
                    colorFilter = ColorFilter.tint(color = HeaderMainColorMain),
                )

                // Кнопка настройки
                Image(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = stringResource(id = R.string.imageDescriptionBack),
                    modifier = Modifier
                        .padding(all = ComponentDiffNormal)
                        .clickable { navController.navigate(NavRoute.SettingInfo.route) },
                    colorFilter = ColorFilter.tint(color = HeaderMainColorMain),
                )
            }
        },
        content = {

            //Главное содержимое
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = ScreenArea)
                    .padding(top = it.calculateTopPadding())
                    .verticalScroll(rememberScrollState())
            ) {
                // Пробел
                Spacer(modifier = Modifier.padding(top = ScreenArea))

                // Внутреннее содержимое
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(largeShape)
                        ),
                ) {
                    AccountCard()
                }

                // Пробел
                Spacer(modifier = Modifier.padding(bottom = ScreenArea))
            }
        }
    )

}

@Composable
fun AccountCard(){
    val cardElevation = 5.dp
    val privacyTop = 10.dp
    val privacyStart = 5.dp
    val photoPadding = 5.dp
    val photoSize = 100.dp

    Card(
        shape = RoundedCornerShape(size = analyticsBig),
        modifier = Modifier.fillMaxWidth(), elevation = cardElevation){
        Row(modifier = Modifier.fillMaxWidth()) {
            //Изображение человека
            Image(
                painter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.account)),
                contentDescription = stringResource(id = R.string.imageDescriptionMyPhoto),
                modifier = Modifier
                    .size(photoSize)
                    .padding(photoPadding)
            )

            //Личная информация
            Column(modifier = Modifier.padding(top = privacyTop, start = privacyStart)) {
                Text(
                    text = stringResource(id = R.string.imageDescriptionNickname),
                    fontSize = NormalFont,
                    fontWeight = FontWeight.Bold
                )
                Text(stringResource(id = R.string.id))
                Text(stringResource(id = R.string.phoneNumber))
            }
        }
    }
}
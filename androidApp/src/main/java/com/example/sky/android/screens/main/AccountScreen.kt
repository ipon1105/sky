package com.example.sky.android.screens

import com.example.sky.android.R
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sky.ui.theme.ScreenArea
import com.example.sky.ui.theme.analyticsBig
import com.example.sky.ui.theme.NormalFont

@Composable
fun AccountScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
            .padding(ScreenArea)
    ) {
        // Кнопка назад
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = rememberVectorPainter(image = Icons.Filled.Settings),
                contentDescription = stringResource(id = R.string.imageDescriptionBack),
                modifier = Modifier.clickable { /*TODO: Сделать переход на настройки*/ }
            )
        }

        // Вызовы Аналитических блоков (сейчас только один)
        AccountCard()
    }
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
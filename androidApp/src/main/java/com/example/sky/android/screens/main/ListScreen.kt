package com.example.sky.android.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import com.example.sky.android.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.sky.android.models.FlatModel
import com.example.sky.android.models.flatList
import com.example.sky.navigation.NavRoute
import com.example.sky.ui.theme.*

@SuppressLint("SuspiciousIndentation")
@Composable
fun ListScreen(navController: NavHostController) {
    val buttonsElevation = 5.dp
    var search by remember {
        mutableStateOf("")
    }
    var btnFree by remember {
        mutableStateOf( true)
    }
    var btnDirty by remember {
        mutableStateOf( true)
    }
    var btnBusy by remember {
        mutableStateOf( true)
    }

    // TODO: Добавить пояснение для кружочков в элементах списка
    // TODO: Оступ в элементе списка справа больше, чем слевва - Сделать одинаковыми.
    // TODO: Попробовать изменить кружочек на флажок, что бы была другая асоциация

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(ScreenArea)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(largeShape)
            )
    ) {
        // Строка с кнопками
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ComponentDiffNormal),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            // Свободно
            Button(
                onClick = { btnFree = !btnFree },
                colors = ButtonDefaults.buttonColors(backgroundColor = if (btnFree) FlatGreen else DarkGray),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = buttonsElevation
                ),
            ) {
                Text(text = stringResource(id = R.string.free), color = Color.White)
            }

            // Грязно
            Button(
                onClick = { btnDirty = !btnDirty },
                colors = ButtonDefaults.buttonColors(backgroundColor = if (btnDirty) FlatYellow else DarkGray),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = buttonsElevation
                ),
            ) {
                Text(text = stringResource(id = R.string.dirty), color = Color.White)
            }

            // Занято
            Button(
                onClick = { btnBusy = !btnBusy },
                colors = ButtonDefaults.buttonColors(backgroundColor = if (btnBusy) FlatRed else DarkGray),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = buttonsElevation
                ),
            ) {
                Text(text = stringResource(id = R.string.busy), color = Color.White)
            }

        }

        // Поле ввода поиска
        TextField(
            value = search,
            onValueChange = {
                search = it
            },
            placeholder = { Text(text = stringResource(id = R.string.search), color = TextSearchColor) },
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(horizontal = ComponentDiffNormal)
                .scale(scaleY = 0.9f, scaleX = 1.0f),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = lightGray,
                focusedIndicatorColor = Transparent,
                unfocusedIndicatorColor = Transparent
            ),
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = stringResource(id = R.string.imageDescriptionEmail)) },
            trailingIcon = {
                if (search.isNotEmpty())
                    IconButton(onClick = { search = ""}) {
                        Icon(imageVector = Icons.Outlined.Close, contentDescription = null)
                    }
            },
            shape = RoundedCornerShape(analyticsBig),
        )

        LazyColumn(modifier = Modifier.padding(horizontal = ComponentDiffNormal)){

            itemsIndexed(
                items = flatList
            ){ index, item ->
                if (item == null)
                    AddNewFlat(navController = navController)
                else if (item.address.contains(search, ignoreCase = true))
                        FlatElementList(item, navController = navController)

            }
        }

        // Пробел
        Spacer(modifier = Modifier.padding(bottom = ScreenArea))
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun FlatElementList(el: FlatModel, navController: NavHostController){
    val cardElevation = 15.dp
    val cardSize = 100.dp
    val status = 30.dp

    // ModelView
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = verticalNormal)
            .clickable { navController.navigate(route = NavRoute.FlatInfo.route) },
        shape = RoundedCornerShape(size = largeShape),
        elevation = cardElevation,
        backgroundColor = lightDarkGray,
    ) {
        Row(
        ) {
            //Изображение квартиры
            Box(
                modifier = Modifier
                    .size(cardSize)
                    .background(color = DarkGray, shape = RoundedCornerShape(size = largeShape))
            ){

                Image(
                    //TODO: Сделать загрузку изображений из базы данных firebase
                    painter = rememberImagePainter("https://picsum.photos/300/300"),
                    contentDescription = stringResource(id = R.string.imageDescriptionFlatPhoto),
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(size = largeShape)),
                )
            }

            // Подробнее
            Column {

                Row(modifier = Modifier.fillMaxWidth()) {

                    // Название квартиры (улица)
                    Text(
                        modifier = Modifier
                            .padding(start = ComponentDiffSmall, top = ComponentDiffSmall)
                            .weight(9f),
                        text = el.address.uppercase(),
                        color = GrayTextColor,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )

                    //Статус
                    Box(
                        modifier = Modifier
                            .size(status)
                            .background(
                                shape = RoundedCornerShape(bottomStart = shortShape),
                                color = FlatGreen
                            )
                            .weight(1.28f)
                    ){}
                }

                // Описание квартиры
                Text(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(ComponentDiffSmall),
                    maxLines = 3,
                    text = el.description,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = true,
                    color = GrayTextColor,
                )
            }

        }
    }
}

@Composable
fun AddNewFlat(navController: NavHostController){
    val cardSize = 100.dp
    val imgSize = 40.dp
    val borderWeight = 8.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = verticalNormal)
            .clickable { navController.navigate(route = NavRoute.Editor.route) }
            .height(cardSize)
            .border(
                border = BorderStroke(borderWeight, color = lightGray),
                shape = RoundedCornerShape(size = largeShape)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(imgSize),
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.imageDescriptionAdd),
            tint = lightGray
        )
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
fun Preview(){
    ListScreen(rememberNavController())
}
package com.example.sky.android.screens.main

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sky.android.R
import com.example.sky.ui.theme.*
import com.google.accompanist.pager.*

@ExperimentalPagerApi
@Composable
private fun ViewPagerSlider(){
    // Из одной квартиры выносит список квартир, которые собирается отображать (в виде ссылок для скачивания)
    val imageList = listOf(
        "Photo_1",
        "Photo_2",
        "Photo_3",
        "Photo_4",
        "Photo_5",
    )
    val maxHorizontalPagerHeight = 250.dp

    val pagerState = rememberPagerState(initialPage = 0)

    /*
    // Автоматическое перелистывание
    LaunchedEffect(Unit){
        while (true) {
            yield()
            delay(2000)
            pagerState.animateScrollToPage(
                page = (pagerState.currentPage + 1) % pagerState.pageCount,
                animationSpec = tween(1000)
            )
        }
    }
    */

    Column(modifier = Modifier.fillMaxWidth().padding(top = ComponentDiffLarge)) {
        HorizontalPager(count = imageList.size,state = pagerState, modifier = Modifier.height(height = maxHorizontalPagerHeight)) { page ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ComponentDiffLarge),
                shape = RoundedCornerShape(canvasShape),
                backgroundColor = DarkGray
            ) {
                val newImgae = imageList[page]
                Box(modifier = Modifier.fillMaxSize()){
                    //TODO: Добавить загрузку иконки (Image(painter = painterResource(id = arr.photo)))
                    //TODO: Растянуть изображение на всю ширину

                    // Изображение
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.map),
                        contentDescription = stringResource(id = R.string.imageDescriptionFlatPhoto)
                    )
                }
            }

        }

        // Точечный индикатор под слайдером изображений
        HorizontalPagerIndicator(pagerState = pagerState,modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = verticalNormal))

        // Панель под Слайдером
        Row (modifier = Modifier.fillMaxWidth().padding(top = verticalNormal), horizontalArrangement = Arrangement.Center){

            // Вспомогательная панель
            Row(modifier = Modifier.align(Alignment.CenterVertically)) {
                // Изображение для Редактирования
                Image(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = stringResource(id = R.string.imageDescriptionEdit),
                    modifier = Modifier.size(iconSizeNormal),
                    colorFilter = ColorFilter.tint(FlatYellow),
                )

                // Пробел
                Spacer(modifier = Modifier.width(ComponentDiffSmall))

                // Изображение для удаления
                Image(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(id = R.string.imageDescriptionDelete),
                    modifier = Modifier.size(iconSizeNormal),
                    colorFilter = ColorFilter.tint(FlatRed),
                )

                // Пробел
                Spacer(modifier = Modifier.width(ComponentDiffSmall))

                // Изображение для Добавления
                Image(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.imageDescriptionAdd),
                    modifier = Modifier.size(iconSizeNormal),
                    colorFilter = ColorFilter.tint(FlatGreen),
                )
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun FlatEditorScreen(navController: NavHostController) {
    var isEditable by remember { mutableStateOf(false) }
    var isEditableDialogShow by remember { mutableStateOf(false) }

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            backgroundColor = HeaderMainColorMain,
            drawerShape =  RoundedCornerShape(
                bottomStart = topBarCornerShape,
                bottomEnd = topBarCornerShape
            ),
            topBar = {
                //Кнопка назад
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
                    horizontalArrangement = Arrangement.Start
                ){
                    Image(
                        painter = rememberVectorPainter(image = Icons.Filled.ArrowBack),
                        contentDescription = stringResource(id = R.string.imageDescriptionBack),
                        modifier = Modifier.clickable {
                            // Диалоговое окно
                            if (isEditable) {
                                isEditableDialogShow = true
                            } else
                                navController.popBackStack()
                        }
                            .padding(all = ComponentDiffNormal)
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
                ){
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
                        // TODO: Сделать проверку на корректность адреса
                        val maxDescriptionHeight = 150.dp
                        val maxAddressLength = 255
                        val maxCostLength = 16
                        val maxDescriptionLength = 512
                        var address by remember { mutableStateOf("") }
                        var cost by remember { mutableStateOf("") }
                        var description by remember { mutableStateOf("") }
                        val isAddressValid by derivedStateOf { address.length < maxAddressLength }
                        val isCostValid by derivedStateOf { cost.length < maxCostLength }
                        val isDescriptionValid by derivedStateOf { description.length < maxDescriptionLength }


                        // Список изображений
                        ViewPagerSlider()

                        // Адрес
                        Column(modifier = Modifier.fillMaxWidth().padding(ComponentDiffNormal)) {
                            // Текст подсказка
                            Text(
                                text = stringResource(id = R.string.address),
                                fontSize = SmallFont,
                                textAlign = TextAlign.Start,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )

                            // Поле ввода Адреса
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White),
                                value = address,
                                onValueChange =
                                { address = it; isEditable = true },
                                placeholder = { Text(text = stringResource(id = R.string.address), fontSize = SmallFont) },
                                singleLine = true,
                                colors = TextFieldDefaults.textFieldColors(
                                    cursorColor = Color.Black,
                                    backgroundColor = lightGray,
                                    disabledLabelColor = lightGray,
                                    focusedIndicatorColor = Transparent,
                                    unfocusedIndicatorColor = Transparent
                                ),
                                shape = RoundedCornerShape(shortShape),
                                isError = !isAddressValid,
                                trailingIcon = {
                                    if (address.isNotEmpty())
                                        IconButton(onClick = { address = ""; isEditable = true }) {
                                            Icon(imageVector = Icons.Outlined.Close, contentDescription = null)
                                        }
                                }
                            )

                            // Тексты под полем ввода
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = ComponentDiffSmallSmall)){
                                val errorColor = if (!isAddressValid) MaterialTheme.colors.error else Transparent

                                // Ошибка
                                Text(
                                    text = stringResource(id = R.string.addressError),
                                    color = errorColor,
                                    style = MaterialTheme.typography.caption,
                                    modifier = Modifier
                                        .padding(start = ErrorStart)
                                        .fillMaxWidth(0.75f),
                                    textAlign = TextAlign.Start,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )

                                // Количество допустимых символов
                                Text(
                                    text = "${address.length} / $maxAddressLength",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.End,
                                    color = Color.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }

                        }

                        // Стоимость  уборки
                        Column(modifier = Modifier.fillMaxWidth().padding(ComponentDiffNormal)) {

                            // Текст подсказка
                            Text(
                                text = stringResource(id = R.string.cleaningCost),
                                fontSize = SmallFont,
                                textAlign = TextAlign.Start,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )

                            // Поле ввода Адреса
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White),
                                value = cost,
                                onValueChange = { cost = it; isEditable = true },
                                placeholder = { Text(text = stringResource(id = R.string.cleaningCostTemplate), fontSize = SmallFont) },
                                singleLine = true,
                                colors = TextFieldDefaults.textFieldColors(
                                    cursorColor = Color.Black,
                                    backgroundColor = lightGray,
                                    disabledLabelColor = lightGray,
                                    focusedIndicatorColor = Transparent,
                                    unfocusedIndicatorColor = Transparent
                                ),
                                shape = RoundedCornerShape(shortShape),
                                isError = !isCostValid,
                                trailingIcon = {
                                    if (cost.isNotEmpty())
                                        IconButton(onClick = { cost = ""; isEditable = true }) {
                                            Icon(imageVector = Icons.Outlined.Close, contentDescription = null)
                                        }
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            )

                            // Тексты под полем ввода
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = ComponentDiffSmallSmall)){
                                val errorColor = if (!isCostValid) MaterialTheme.colors.error else Transparent

                                // Ошибка
                                Text(
                                    text = stringResource(id = R.string.cleaningCostError),
                                    color = errorColor,
                                    style = MaterialTheme.typography.caption,
                                    modifier = Modifier
                                        .padding(start = ErrorStart)
                                        .fillMaxWidth(0.75f),
                                    textAlign = TextAlign.Start,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )

                                // Количество допустимых символов
                                Text(
                                    text = "${cost.length} / $maxCostLength",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.End,
                                    color = Color.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }

                        // Описание
                        Column(modifier = Modifier.fillMaxWidth().padding(ComponentDiffNormal)){
                            // Текст подсказка
                            Text(
                                text = stringResource(id = R.string.description),
                                fontSize = SmallFont,
                                textAlign = TextAlign.Start,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )

                            // Поле ввода Описания
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(maxDescriptionHeight)
                                    .background(Color.White),
                                value = description,
                                onValueChange = { description = it; isEditable = true },
                                placeholder = { Text(text = stringResource(id = R.string.descriptionTemplate), fontSize = SmallFont) },
                                colors = TextFieldDefaults.textFieldColors(
                                    cursorColor = Color.Black,
                                    backgroundColor = lightGray,
                                    disabledLabelColor = lightGray,
                                    focusedIndicatorColor = Transparent,
                                    unfocusedIndicatorColor = Transparent
                                ),
                                shape = RoundedCornerShape(shortShape),
                                isError = !isDescriptionValid,
                            )

                            // Тексты под полем ввода
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = ComponentDiffSmallSmall)){
                                val errorColor = if (!isDescriptionValid) MaterialTheme.colors.error else Transparent

                                // Ошибка
                                Text(
                                    text = stringResource(id = R.string.descriptionError),
                                    color = errorColor,
                                    style = MaterialTheme.typography.caption,
                                    modifier = Modifier
                                        .padding(start = ErrorStart)
                                        .fillMaxWidth(0.75f),
                                    textAlign = TextAlign.Start,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )

                                // Количество допустимых символов
                                Text(
                                    text = "${description.length} / $maxDescriptionLength",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.End,
                                    color = Color.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }

                        }

                        // Кнопки управления
                        Row(modifier = Modifier.fillMaxWidth().padding(ComponentDiffLarge), horizontalArrangement = Arrangement.SpaceEvenly){
                            // Кнопка Отмены
                            Button(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(size = largeShape),
                                colors = ButtonDefaults.buttonColors( backgroundColor = FlatRed ),
                                onClick = {
                                    if (isAddressValid && isCostValid && isDescriptionValid){
                                        //TODO: Сделать отмену и вернуть исходные данные
                                    }
                                },
                            ) {
                                Text(text = stringResource(id = R.string.cancel), color = Color.White, modifier = Modifier.padding(all = ButtonArea))
                            }

                            // Пробел
                            Spacer(modifier = Modifier.weight(0.4f))

                            // Кнопка Действия
                            Button(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(size = largeShape),
                                colors = ButtonDefaults.buttonColors( backgroundColor = FlatGreen ),
                                onClick = {
                                    if (isAddressValid && isCostValid && isDescriptionValid){
                                        //TODO: Принять изменения и внести их в базу данных
                                    }
                                },
                            ) {
                                Text(text = stringResource(id = R.string.apply), color = Color.White, modifier = Modifier.padding(all = ButtonArea), )
                            }
                        }

                        //Дилоговое окно
                        if (isEditableDialogShow)
                            AlertDialog(
                                onDismissRequest = { navController.popBackStack() },
                                title = { Text(text = stringResource(id = R.string.warning), color = Color.Yellow) },
                                text = { Text( text  = stringResource(id = R.string.flatEditorBackWarning) ) },
                                buttons = {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(all = alertDialogArea)
                                            .padding(top = 0.dp),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Button(
                                            onClick = { isEditableDialogShow = false; navController.popBackStack() },
                                            shape = RoundedCornerShape(largeShape),
                                            colors = ButtonDefaults.buttonColors( backgroundColor = mainColor ),
                                        ) {
                                            Text( text = stringResource(id = R.string.alertYes), color = Color.White, modifier = Modifier.padding(all = TextTopSmall))
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Button(
                                            onClick = { isEditableDialogShow = false },
                                            shape = RoundedCornerShape(largeShape),
                                            colors = ButtonDefaults.buttonColors( backgroundColor = mainColor ),
                                        ) {
                                            Text( text = stringResource(id = R.string.alertNo), color = Color.White, modifier = Modifier.padding(all = TextTopSmall))
                                        }
                                    }
                                }
                            )
                    }

                    // Пробел
                    Spacer(modifier = Modifier.padding(bottom = ScreenArea))
                }



            }
        )

}

package com.example.sky.android.screens.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.sky.android.R
import com.example.sky.android.composables.ui.CustomTextField
import com.example.sky.android.composables.ui.ProgressButton
import com.example.sky.android.models.SettingViewModel
import com.example.sky.android.models.data.UserData
import com.example.sky.ui.theme.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SettingScreen(navController: NavHostController){
    val viewModel = viewModel<SettingViewModel>()

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
                Spacer(modifier = Modifier.padding(top = CardTopPaddingNormal))

                // Настройки приложения
                GeneralSettingsCard()

                // Отступ
                Spacer(modifier = Modifier.padding(top = CardPaddingNormal))

                // Настройки работников
                if (viewModel.status == 2)
                    WorkerSettingsCard(navController, viewModel)

                // Пробел
                Spacer(modifier = Modifier.padding(bottom = CardBottomPaddingNormal))
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GeneralSettingsCard(){
    val cardElevation = 5.dp
    var notificationState by remember { mutableStateOf(true) }
    var telephoneState by remember { mutableStateOf(true) }
    var isThemeLight by remember { mutableStateOf(true) }

    // Карточка
    Card(
        shape = RoundedCornerShape(size = analyticsBig),
        modifier = Modifier.fillMaxWidth(), elevation = cardElevation)
    {
        // Содержимое карточки
        Column(
            Modifier
                .fillMaxWidth()
                .padding(ComponentDiffSmall)) {
            // Заголовок
            Text(
                text = stringResource(id = R.string.genSetting),
                style = TextStyle(
                    color = HeaderMainColorMain,
                    fontSize = LargeFont
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )

            // TODO: Сделать настройку уведомление
            // Контейнер с Чекбоксом
            Row(modifier = Modifier
                .padding(top = ComponentDiffNormal, start = ComponentDiffNormal)
                .clickable { notificationState = !notificationState }){
                // Уведомления
                CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false){
                    Checkbox(
                        checked = notificationState,
                        onCheckedChange = { notificationState = !notificationState },
                        colors = CheckboxDefaults.colors(
                            uncheckedColor = DarkGray,
                            checkedColor = HeaderMainColorMain,
                        )
                    )
                }

                // Текст подсказка чекбокса
                Text(
                    text = stringResource(id = R.string.genNotifications),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = ComponentDiffNormal),
                    color = GrayTextColor
                )
            }

            // TODO: Сделать настройку Скрыть номер телефона
            // Контейнер с Чекбоксом
            Row(modifier = Modifier
                .padding(top = ComponentDiffNormal, start = ComponentDiffNormal)
                .clickable { telephoneState = !telephoneState }){
                // Сркыть номер телефона
                CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false){
                    Checkbox(
                        checked = telephoneState,
                        onCheckedChange = { telephoneState = !telephoneState },
                        colors = CheckboxDefaults.colors(
                            uncheckedColor = DarkGray,
                            checkedColor = HeaderMainColorMain,
                        )
                    )
                }

                // Текст подсказка чекбокса
                Text(
                    text = stringResource(id = R.string.genTelephone),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = ComponentDiffNormal),
                    color = GrayTextColor
                )
            }

            // TODO: Сделать смену темы приложения
            // Контейрнер групирующий радио кнопки
            Column(modifier = Modifier
                .padding(top = ComponentDiffNormal, start = ComponentDiffNormal)
                .selectableGroup()
            ) {
                // Контейнр компонента
                Row(modifier = Modifier.clickable { isThemeLight = true }) {
                    // Светлая тема
                    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false){
                        RadioButton(
                            selected = isThemeLight,
                            colors = RadioButtonDefaults.colors(
                                unselectedColor = DarkGray,
                                selectedColor = HeaderMainColorMain,
                            ),
                            onClick = { isThemeLight = true },
                        )
                    }

                    // Текс подсказка
                    Text(
                        text = stringResource(id = R.string.lightTheme),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = ComponentDiffNormal),
                        color = GrayTextColor
                    )
                }

                // Контейнр компонента
                Row(modifier = Modifier
                    .padding(top = ComponentDiffSmall)
                    .clickable { isThemeLight = false }) {
                    // Тёмная тема
                    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false){
                        RadioButton(
                            selected = !isThemeLight,
                            onClick = { isThemeLight = false },
                            colors = RadioButtonDefaults.colors(
                                unselectedColor = DarkGray,
                                selectedColor = HeaderMainColorMain,
                            )
                        )
                    }

                    // Текс подсказка
                    Text(
                        text = stringResource(id = R.string.darkTheme),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = ComponentDiffNormal),
                        color = GrayTextColor
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkerSettingsCard(navController: NavHostController, viewModel: SettingViewModel){
    val maxLazyHeight = 300.dp
    val cardElevation = 5.dp

    // Карточка
    Card(
        shape = RoundedCornerShape(size = analyticsBig),
        modifier = Modifier.fillMaxWidth(),
        elevation = cardElevation,
    )
    {
        // Содержимое карточки
        Column(
            Modifier
                .fillMaxWidth()
                .padding(ComponentDiffSmall)
        ) {
            // Заголовок
            Text(
                text = stringResource(id = R.string.workerSetting),
                style = TextStyle(
                    color = HeaderMainColorMain,
                    fontSize = LargeFont
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )

            // Список работников
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = ComponentDiffNormal)
                    .height(maxLazyHeight)
                    .padding(top = ComponentDiffNormal)
            ){
                itemsIndexed(items = viewModel.workerList + null){index, item ->
                    if (item == null)
                        AddNewWorker(navController = navController, viewModel)
                    else
                        WorkerCard(navController = navController, item, viewModel, index)
                }
            }
        }
    }
    
    if (viewModel.workerDialog){
        AlertDialog(
            onDismissRequest = { viewModel.newWorker(false) },
            title = { Text(text = "Добавить нового работника") },
            text = {
                CustomTextField(
                    text = viewModel.value,
                    onValueChange = { viewModel.newValue(it) },
                    placeholderText = "Введите id работника",
                    fontSize = 24.sp
                )
            },
            buttons = {
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    ProgressButton(onClick = { viewModel.newWorkerAdd( ) }, text = "Добавить", modifier = Modifier.padding(
                        bottom = ComponentDiffNormal,
                        end = ComponentDiffNormal
                    ))
                }
            }
        )
    }
}


@Composable
fun WorkerCard(navController: NavHostController, userData: UserData, viewModel: SettingViewModel, index: Int){
    val cardElevation = 15.dp
    val cardSize = 80.dp
    val imageSize = 50.dp
    val difSizes = 15.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardSize)
            .padding(vertical = verticalNormal)
            .clickable { },
        shape = RoundedCornerShape(size = largeShape),
        elevation = cardElevation,
        backgroundColor = lightDarkGray,
    ) {
        // Главное содержимое
        Row(modifier = Modifier.fillMaxSize()){
            // Изображение работника
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.no_image),
                contentDescription = stringResource(id = R.string.imageDescriptionFlatPhoto),
                modifier = Modifier
                    .padding(start = difSizes)
                    .size(imageSize)
                    .clip(shape = CircleShape)
                    .background(color = DarkGray)
                    .align(Alignment.CenterVertically),
                tint = Color.White,
            )

            // Правая часть содержимого
            Row(Modifier.fillMaxSize()){
                // ФИО
                Column(
                    modifier = Modifier
                        .padding(start = ComponentDiffSmall, top = ComponentDiffSmall)
                        .weight(9f)
                        .clickable { viewModel.detail(navController, index) },
                ) {
                    // ФИО
                    Text(
                        text = "${userData.surname} ${userData.name} ${userData.patronymic}",
                        color = GrayTextColor,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )

                    // Телефон
                    Text(
                        text = "Телефон ${userData.telephone}",
                        color = GrayTextColor,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                }

                // Удалить
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(id = R.string.imageDescriptionDelete),
                    modifier = Modifier
                        .size(imageSize)
                        .weight(1.28f)
                        .align(Alignment.CenterVertically)
                        .clickable { viewModel.deleteElement(index) },
                    tint = FlatRed,
                )
            }
        }
    }
}

@Composable
fun AddNewWorker(navController: NavHostController, viewModel: SettingViewModel){
    val cardSize = 80.dp
    val imgSize = 30.dp
    val borderWeight = 8.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = verticalNormal)
            .clickable { viewModel.newWorker(true) }
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
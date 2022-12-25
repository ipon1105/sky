package com.example.sky.android.screens.main

import android.widget.RatingBar
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.sky.android.R
import com.example.sky.android.models.WorkerModel
import com.example.sky.android.composables.RatingBar
import com.example.sky.android.models.authorization.LoginViewModel
import com.example.sky.android.models.data.SettingViewModel
import com.example.sky.navigation.NavRoute
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
    var isFreeShow by remember{mutableStateOf(true)}
    var isDirtyShow by remember{mutableStateOf(true)}
    var isBusyShow by remember{mutableStateOf(true)}

    // TODO: Подгружать список работников из интернета
    var workers = listOf(
        WorkerModel(1u, "photo1", "Name", "Surname", "Patronymicddddddddddddddddddddddddddddddddddddd", "Description"),
        WorkerModel(2u, "photo2", "Name", "Surname", "Patronymic", "Description"),
        WorkerModel(5u, "photo2", "Name", "Surname", "Patronymic", "Description"),
        WorkerModel(5u, "photo2", "Name", "Surname", "Patronymic", "Description"),
        WorkerModel(5u, "photo2", "Name", "Surname", "Patronymic", "Description"),
        null
    )

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

            // TODO: Сделать настройку доступа к свободным квартирам
            // Контейнер с Чекбоксом
            Row(modifier = Modifier
                .padding(top = ComponentDiffNormal, start = ComponentDiffNormal)
                .clickable { isFreeShow = !isFreeShow }
            ){
                // Свободные
                CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false){
                    Checkbox(
                        checked = isFreeShow,
                        onCheckedChange = { isFreeShow = !isFreeShow },
                        colors = CheckboxDefaults.colors(
                            uncheckedColor = DarkGray,
                            checkedColor = HeaderMainColorMain,
                        )
                    )
                }

                // Текст подсказка чекбокса
                Text(
                    text = stringResource(id = R.string.workerSeeFree),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = ComponentDiffNormal),
                    color = GrayTextColor
                )
            }

            // TODO: Сделать настройку доступа к грязным квартирам
            // Контейнер с Чекбоксом
            Row(modifier = Modifier
                .padding(top = ComponentDiffNormal, start = ComponentDiffNormal)
                .clickable { isDirtyShow = !isDirtyShow }
            ){
                // Грязные
                CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false){
                    Checkbox(
                        checked = isDirtyShow,
                        onCheckedChange = { isDirtyShow = !isDirtyShow },
                        colors = CheckboxDefaults.colors(
                            uncheckedColor = DarkGray,
                            checkedColor = HeaderMainColorMain,
                        )
                    )
                }

                // Текст подсказка чекбокса
                Text(
                    text = stringResource(id = R.string.workerSeeDirty),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = ComponentDiffNormal),
                    color = GrayTextColor
                )
            }

            // TODO: Сделать настройку доступа к занятым квартирам
            // Контейнер с Чекбоксом
            Row(modifier = Modifier
                .padding(top = ComponentDiffNormal, start = ComponentDiffNormal)
                .clickable { isBusyShow = !isBusyShow }
            ){
                // Занятые
                CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false){
                    Checkbox(
                        checked = isBusyShow,
                        onCheckedChange = { isBusyShow = !isBusyShow },
                        colors = CheckboxDefaults.colors(
                            uncheckedColor = DarkGray,
                            checkedColor = HeaderMainColorMain,
                        )
                    )
                }

                // Текст подсказка чекбокса
                Text(
                    text = stringResource(id = R.string.workerSeeBusy),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = ComponentDiffNormal),
                    color = GrayTextColor
                )
            }

            // Список работников
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = ComponentDiffNormal)
                    .height(maxLazyHeight)
                    .padding(top = ComponentDiffNormal)
            ){
                itemsIndexed(items = workers){index, item ->
                    if (item == null)
                        AddNewWorker(navController = navController)
                    else
                        WorkerCard(navController = navController, item)
                }
            }
        }
    }
}

@Composable
fun WorkerCard(navController: NavHostController, worker: WorkerModel){
    val cardElevation = 15.dp
    val cardSize = 80.dp
    val imageSize = 50.dp
    val difSizes = 15.dp

    // TODO: Сделать переход на аккаунт работника по клику на карточку
    // ModelView
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
            Image(
                //TODO: Сделать загрузку изображения из базы данных firebase
                imageVector = ImageVector.vectorResource(id = R.drawable.map),
                contentDescription = stringResource(id = R.string.imageDescriptionFlatPhoto),
                modifier = Modifier
                    .padding(start = difSizes)
                    .size(imageSize)
                    .clip(shape = CircleShape)
                    .background(color = DarkGray)
                    .align(Alignment.CenterVertically)
            )

            // Правая часть содержимого
            Row(Modifier.fillMaxSize()){
                // ФИО
                Column(
                    modifier = Modifier
                        .padding(start = ComponentDiffSmall, top = ComponentDiffSmall)
                        .weight(9f),
                ) {
                    // ФИО
                    Text(
                        text = "${worker.surname} ${worker.name} ${worker.patronymic}",
                        color = GrayTextColor,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )

                    // Телефон
                    Text(
                        text = "Телефон ${worker.description}",
                        color = GrayTextColor,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                }

                // TODO: Сделать удаление работника из списка работников
                // TODO: Сделать диалог с вопросом о удалении работника
                // Удалить
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(id = R.string.imageDescriptionDelete),
                    modifier = Modifier
                        .size(imageSize)
                        .weight(1.28f)
                        .align(Alignment.CenterVertically),
                    tint = FlatRed,

                )
            }
        }
    }
}

@Composable
fun AddNewWorker(navController: NavHostController){
    val cardSize = 80.dp
    val imgSize = 30.dp
    val borderWeight = 8.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = verticalNormal)
            .clickable { navController.navigate(route = NavRoute.WorkerSearch.route) }
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
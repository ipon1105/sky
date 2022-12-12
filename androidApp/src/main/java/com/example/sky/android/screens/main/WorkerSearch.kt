package com.example.sky.android.screens.main

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sky.android.R
import com.example.sky.android.composables.RatingBar
import com.example.sky.android.models.WorkerModel
import com.example.sky.navigation.NavRoute
import com.example.sky.ui.theme.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun WorkerSearchScreen(navController: NavHostController){

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

                // Поисковой блок
                workerSearch(navController = navController)

                // Пробел
                Spacer(modifier = Modifier.padding(bottom = CardBottomPaddingNormal))
            }
        }
    )
}

@Composable
fun workerSearch(navController: NavHostController){
    val maxLazyHeight = 600.dp
    val cardElevation = 5.dp
    var search by remember {
        mutableStateOf("")
    }
    val db = Firebase.firestore

    // TODO: Подгружать список работников из интернета по несколько штук
    var workers = listOf(
        WorkerModel(1u, "photo1", "Name", "Surname", "Patronymicddddddddddddddddddddddddddddddddddddd", "DescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescription"),
        WorkerModel(2u, "photo2", "Name", "Surname", "Patronymic", "Description"),
        WorkerModel(3u, "photo2", "Name", "Surname", "Patronymic", "Description"),
        WorkerModel(4u, "photo2", "Name", "Surname", "Patronymic", "Description"),
        WorkerModel(5u, "photo2", "Name", "Surname", "Patronymic", "Description"),
        WorkerModel(5u, "photo2", "Name", "Surname", "Patronymic", "Description"),
        WorkerModel(5u, "photo2", "Name", "Surname", "Patronymic", "Description"),
        WorkerModel(5u, "photo2", "Name", "Surname", "Patronymic", "Description"),
        WorkerModel(5u, "photo2", "Name", "Surname", "Patronymic", "Description"),
        WorkerModel(5u, "photo2", "Name", "Surname", "Patronymic", "Description"),
        WorkerModel(5u, "photo2", "Name", "Surname", "Patronymic", "Description"),
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
            // TODO: Сделать поиск работников, как по ID так и по ФИО
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
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
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

            // Список всех работников
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = ComponentDiffNormal)
                    .height(maxLazyHeight)
                    .padding(top = ComponentDiffNormal)
            ){
                itemsIndexed(items = workers){index, item ->
                    if (item != null)
                        workerCard(navController, item)

                }
            }
        }
    }
}

@Composable
fun workerCard(navController: NavHostController, worker:WorkerModel){
    val cardElevation = 15.dp
    val cardSize = 100.dp
    val status = 30.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = verticalNormal)
            .clickable { /*TODO: Сделать переход на страницу аккаунта*/ }
            .height(cardSize),
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
                    imageVector = ImageVector.vectorResource(id = R.drawable.map),
                    contentDescription = stringResource(id = R.string.imageDescriptionFlatPhoto),
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(size = largeShape)),
                )
            }

            // Подробнее
            Column(
                modifier = Modifier
                    .padding(ComponentDiffSmall)
            ) {
                // ФИО
                Text(
                    text = "${worker.surname} ${worker.name} ${worker.patronymic}",
                    color = GrayTextColor,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )

                // TODO: Разобраться с рейтингом работника
                // Оценка
                RatingBar(modifier = Modifier
                    .height(17.dp)
                    .width(85.dp), rating = 2.0f, color = RatingBarYellow)

                // Описание
                Text(
                    modifier = Modifier
                        .fillMaxHeight(),
                    maxLines = 3,
                    text = worker.description,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = true,
                    color = GrayTextColor,
                )
            }

        }
    }
}
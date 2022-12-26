package com.example.sky.android.screens.main

import android.annotation.SuppressLint
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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.sky.android.R
import com.example.sky.android.models.data.Worker
import com.example.sky.android.models.WorkerSearchViewModel
import com.example.sky.android.models.data.UserData
import com.example.sky.ui.theme.*

@Composable
fun WorkerSearchScreen(navController: NavHostController){
    var viewModel = viewModel<WorkerSearchViewModel>()

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
                workerSearch(viewModel)

                // Пробел
                Spacer(modifier = Modifier.padding(bottom = CardBottomPaddingNormal))
            }
        }
    )
}

@Composable
fun workerSearch(viewModel: WorkerSearchViewModel){
    val maxLazyHeight = 600.dp
    val cardElevation = 5.dp
    var search by remember {
        mutableStateOf("")
    }

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
                itemsIndexed(items = viewModel.workerList){index, item ->
                    if (item != null)

                        workerCard(item, viewModel.usdataList[index], viewModel)

                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun workerCard(worker: Worker, userData: UserData, viewModel: WorkerSearchViewModel){
    val cardElevation = 15.dp
    val cardSize = 100.dp
    val status = 30.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = verticalNormal)
            .clickable { viewModel.newNotify(worker) }
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
                    imageVector = ImageVector.vectorResource(id = R.drawable.no_image),
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
                    text = "${userData.surname} ${userData.name} ${userData.patronymic}",
                    color = GrayTextColor,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                
                Button(onClick = { viewModel.newNotify(worker) }) {
                    Text(text = "Пригласить")
                }
            }

        }
    }
}
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.sky.android.composables.NoInternet
import com.example.sky.android.models.ListViewModel
import com.example.sky.android.models.data.Flat
import com.example.sky.android.utils.connection.ConnectionState
import com.example.sky.android.utils.connection.connectivityState
import com.example.sky.ui.theme.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

// Обновить данные в вью модели
fun refreshModel(){
    viewModel.refreshList()
}

// вью модель
private val viewModel = ListViewModel()

@OptIn(ExperimentalCoroutinesApi::class)
@SuppressLint("SuspiciousIndentation", "UnrememberedMutableState",
    "StateFlowValueCalledInComposition"
)
@Composable
fun ListScreen(navController: NavHostController) {
    // Обновление списка
    viewModel.updateList()

    // Интернет
    val connection by connectivityState()
    viewModel.setInternet(connection === ConnectionState.Available)

    val buttonsElevation = 5.dp

    // Главное содержимое
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
                onClick = { viewModel.setBtnFreeOn(!viewModel.isBtnFreeOn) },
                colors = ButtonDefaults.buttonColors(backgroundColor = if (viewModel.isBtnFreeOn) FlatGreen else DarkGray),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = buttonsElevation
                ),
            ) {
                Text(text = stringResource(id = R.string.free), color = Color.White)
            }

            // Грязно
            Button(
                onClick = { viewModel.setBtnDirtyOn(!viewModel.isBtnDirtyOn) },
                colors = ButtonDefaults.buttonColors(backgroundColor = if (viewModel.isBtnDirtyOn) FlatYellow else DarkGray),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = buttonsElevation
                ),
            ) {
                Text(text = stringResource(id = R.string.dirty), color = Color.White)
            }

            // Занято
            Button(
                onClick = { viewModel.setBtnBusyOn(!viewModel.isBtnBusyOn) },
                colors = ButtonDefaults.buttonColors(backgroundColor = if (viewModel.isBtnBusyOn) FlatRed else DarkGray),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = buttonsElevation
                ),
            ) {
                Text(text = stringResource(id = R.string.busy), color = Color.White)
            }

        }

        // Поле ввода поиска
        TextField(
            value = viewModel.search,
            onValueChange = { viewModel.setSearch(it) },
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
                if (viewModel.search.isNotEmpty())
                    IconButton(onClick = { viewModel.setSearch("")}) {
                        Icon(imageVector = Icons.Outlined.Close, contentDescription = null)
                    }
            },
            shape = RoundedCornerShape(analyticsBig),
        )

        // Загрузка данных из сети
        if (viewModel.isUpdating)
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally), color =  mainColor )

        // Список квартир
        LazyColumn(modifier = Modifier.padding(horizontal = ComponentDiffNormal)){

            itemsIndexed(
                items = viewModel.flatList
            ){ index, item ->

                if (item == null)
                    AddNewFlat(navController = navController)
                else if (item.address.contains(viewModel.search, ignoreCase = true))
                {
                    if (viewModel.isBtnFreeOn && item.status == 0 )
                        FlatElementList(item, navController = navController)
                    if (viewModel.isBtnDirtyOn && item.status == 1)
                        FlatElementList(item, navController = navController)
                    if (viewModel.isBtnBusyOn && item.status == 2)
                        FlatElementList(item, navController = navController)
                }

            }
        }

        // Пробел
        Spacer(modifier = Modifier.padding(bottom = ScreenArea))
    }

    // Диалоговое окно с ошибкой
    if (viewModel.showDialog && viewModel.internet)
        AlertDialog(
            onDismissRequest = { viewModel.setShowDialog(false) },
            title = { Text(text = stringResource(id = R.string.error), color = Color.Red) },
            text = { Text( text = viewModel.dialogMsg ) },
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
                        shape = RoundedCornerShape(largeShape),
                        colors = ButtonDefaults.buttonColors( backgroundColor = mainColor ),
                    ) {
                        Text( text = stringResource(id = R.string.alertOkay), color = Color.White, modifier = Modifier.padding(all = TextTopSmall))
                    }
                }
            }
        )

    // Сообщение об отсутсвие интернета
    if (viewModel.showDialog && !viewModel.internet)
        NoInternet(onDismissRequest = {viewModel.setShowDialog(false)}, btnOnClick = {viewModel.setShowDialog(false)})
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun FlatElementList(el: Flat, navController: NavHostController){
    val cardElevation = 15.dp
    val cardSize = 100.dp
    val status = 30.dp

    // ModelView
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = verticalNormal)
            .clickable {
                viewModel.cardClickFlatInfo(
                    navController = navController,
                    flatId = el.flatId
                )
            },
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
                // Изображение по умолчаниюы
                if (el.photos.size == 0)
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = ImageVector.vectorResource(id = R.drawable.no_image),
                        contentDescription = stringResource(id = R.string.imageDescriptionFlatPhoto),
                        colorFilter = ColorFilter.tint(Color.White),
                    )

                //Изображение из сети
                else
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
                                color = if (el.status == 0) FlatGreen else if (el.status == 1) FlatYellow else FlatRed
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
            .clickable { viewModel.cardClickToNewFlat(navController = navController) }
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
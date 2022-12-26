package com.example.sky.android.screens.main

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.sky.android.R
import com.example.sky.android.composables.EditorTextField
import com.example.sky.android.composables.NoInternet
import com.example.sky.android.composables.mToast
import com.example.sky.android.models.FlatEditorViewModel
import com.example.sky.android.screens.refreshModel
import com.example.sky.android.utils.connection.ConnectionState
import com.example.sky.android.utils.connection.connectivityState
import com.example.sky.ui.theme.*
import com.google.accompanist.pager.*

@OptIn(ExperimentalGlideComposeApi::class)
@SuppressLint("SuspiciousIndentation", "UnrememberedMutableState")
@ExperimentalPagerApi
@Composable
private fun ViewPagerSlider(viewModel: FlatEditorViewModel){
    val maxHorizontalPagerHeight = 250.dp
    val pagerState = rememberPagerState(initialPage = 0)

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){ uri ->
        Log.d("viewModel", uri.toString())
        if (uri != null) {
            viewModel.addNewImage(uri)
        }
    }

    Log.d("viewModel", "imageList = downloadImageList(${viewModel.downloadImageList.size}) + newImageList(${viewModel.newImageList.size})")

    var imageList: List<Uri?> = viewModel.downloadImageList + viewModel.newImageList
    if (imageList.size == 0)
        imageList += null

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(top = ComponentDiffLarge)) {
        HorizontalPager(count = imageList.size, state = pagerState, modifier = Modifier.height(height = maxHorizontalPagerHeight)) { page ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ComponentDiffLarge),
                shape = RoundedCornerShape(canvasShape),
                backgroundColor = DarkGray
            ) {
                Box(modifier = Modifier.fillMaxSize()){

                    // Модель загружается
                    if (viewModel.isFlatLoading)
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = mainColor)

                    // У модели нет изображений
                    else if (imageList[0] == null)
                    // Изображение по умолчанию
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            imageVector = ImageVector.vectorResource(id = R.drawable.no_image),
                            contentDescription = stringResource(id = R.string.imageDescriptionFlatPhoto),
                            colorFilter = ColorFilter.tint(Color.White),
                        )

                    // У модели есть свои изобржения
                    else
                    // Изображение из сети
                        GlideImage(
                            modifier = Modifier.fillMaxSize(),
                            model = imageList[page],
                            contentDescription = stringResource(id = R.string.imageDescriptionFlatPhoto),
                        )
                }
            }

        }

        // Точечный индикатор под слайдером изображений
        HorizontalPagerIndicator(pagerState = pagerState,modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = verticalNormal))

        // Панель под Слайдером
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(top = verticalNormal), horizontalArrangement = Arrangement.Center){

            // Вспомогательная панель
            Row(modifier = Modifier.align(Alignment.CenterVertically)) {
                // Изображение для удаления
                Image(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(id = R.string.imageDescriptionDelete),
                    colorFilter = ColorFilter.tint(FlatRed),
                    modifier = Modifier
                        .size(iconSizeNormal)
                        .clickable { viewModel.btnDeleteImageClick(pagerState.currentPage) },
                )

                // Пробел
                Spacer(modifier = Modifier.width(ComponentDiffSmall))

                // Изображение для Добавления
                Image(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.imageDescriptionAdd),
                    colorFilter = ColorFilter.tint(FlatGreen),
                    modifier = Modifier
                        .size(iconSizeNormal)
                        .clickable {
                            viewModel.btnAddImageClick(launcher)
                        },
                )
            }
        }
    }
}



@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun FlatEditorScreen(navController: NavHostController, flatId: String = "") {
    var viewModel = FlatEditorViewModel(flatId, LocalContext.current)

    // Интернет
    val connection by connectivityState()
    viewModel.setInternet(connection === ConnectionState.Available)

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
                    modifier = Modifier
                        .clickable { viewModel.btnBackClick(navController) }
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

                    // Для скрытия блока
                    if (!viewModel.isFlatLoading)
                        Column(modifier = Modifier.fillMaxSize()) {

                            // Список изображений
                            ViewPagerSlider(viewModel)

                            // Адрес
                            EditorTextField(
                                hint = stringResource(id = R.string.address),
                                text = viewModel.address,
                                placeholder = stringResource(id = R.string.address),
                                onValueChange = { viewModel.setAddress(it) },
                                isError = !viewModel.isAddressValid,
                                isClear = true,
                                maxLength = viewModel.maxAddressLength,
                                errorMsg = stringResource(id = R.string.addressError)
                            )
// Для следующих версий
//                            // Стоимость  уборки
//                            EditorTextField(
//                                hint = stringResource(id = R.string.cleaningCost),
//                                text = viewModel.cleaningCost,
//                                placeholder = stringResource(id = R.string.cleaningCostTemplate),
//                                onValueChange = { viewModel.setCleaningCost(it) },
//                                isError = !viewModel.isCleaningCostValid,
//                                isClear = true,
//                                maxLength = viewModel.maxCleaningCostLength,
//                                errorMsg = stringResource(id = R.string.cleaningCostError)
//                            )

                            // Описание
                            EditorTextField(
                                hint = stringResource(id = R.string.description),
                                text = viewModel.description,
                                placeholder = stringResource(id = R.string.descriptionTemplate),
                                onValueChange = { viewModel.setDescription(it) },
                                isError = !viewModel.isDescriptionValid,
                                isClear = false,
                                maxLength = viewModel.maxDescriptionLength,
                                errorMsg = stringResource(id = R.string.descriptionError),
                                minHeight = viewModel.maxDescriptionHeight
                            )

                            // Кнопки управления
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(ComponentDiffLarge), horizontalArrangement = Arrangement.SpaceEvenly
                            ){
                                // Кнопка Отмены
                                Button(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(size = largeShape),
                                    colors = ButtonDefaults.buttonColors( backgroundColor = FlatRed ),
                                    onClick = { viewModel.btnCancelClick(navController) },
                                ) {
                                    Text(text = stringResource(id = R.string.cancel), color = Color.White, modifier = Modifier.padding(all = ButtonArea))
                                }

                                // Пробел
                                Spacer(modifier = Modifier.weight(0.4f))

                                // Кнопка Действия
                                Button(
                                    enabled = !viewModel.isUpdation,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(size = largeShape),
                                    colors = ButtonDefaults.buttonColors( backgroundColor = FlatGreen ),
                                    onClick = { viewModel.btnApplyClick() },
                                ) {
                                    Column(modifier = Modifier.fillMaxSize()){
                                        if (viewModel.isUpdation)
                                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally), color =  mainColor )
                                        else
                                            Text(text = stringResource(id = R.string.apply), color = Color.White, modifier = Modifier
                                                .padding(all = ButtonArea)
                                                .align(Alignment.CenterHorizontally), )
                                    }
                                }
                            }
                        }
                    else
                        CircularProgressIndicator(modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(ComponentDiffLarge), color =  mainColor )

                    //Дилоговое окно
                    if (viewModel.isEditableDialogShow)
                        AlertDialog(
                            onDismissRequest = { refreshModel(); navController.popBackStack() },
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
                                        onClick = { viewModel.setEditableDialogShow(false); navController.popBackStack() },
                                        shape = RoundedCornerShape(largeShape),
                                        colors = ButtonDefaults.buttonColors( backgroundColor = mainColor ),
                                    ) {
                                        Text( text = stringResource(id = R.string.alertYes), color = Color.White, modifier = Modifier.padding(all = TextTopSmall))
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Button(
                                        onClick = { viewModel.setEditableDialogShow(false) },
                                        shape = RoundedCornerShape(largeShape),
                                        colors = ButtonDefaults.buttonColors( backgroundColor = mainColor ),
                                    ) {
                                        Text( text = stringResource(id = R.string.alertNo), color = Color.White, modifier = Modifier.padding(all = TextTopSmall))
                                    }
                                }
                            }
                        )

                    //Дилоговое окно
                    if (viewModel.isShowLittleError)
                        AlertDialog(
                            onDismissRequest = { viewModel.setShowLittleError(false) },
                            title = { Text(text = stringResource(id = R.string.warning), color = Color.Yellow) },
                            text = { Text( text  = stringResource(id = R.string.valuesEmpty) ) },
                            buttons = {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(all = alertDialogArea)
                                        .padding(top = 0.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Button(
                                        onClick = { viewModel.setShowLittleError(false) },
                                        shape = RoundedCornerShape(largeShape),
                                        colors = ButtonDefaults.buttonColors( backgroundColor = mainColor ),
                                    ) {
                                        Text( text = stringResource(id = R.string.alertOkay), color = Color.White, modifier = Modifier.padding(all = TextTopSmall))
                                    }
                                }
                            }
                        )

                    // Диалоговое окно
                    if (viewModel.dialogShow)
                        AlertDialog(
                            onDismissRequest = { viewModel.setDialogShow(false) },
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
                                        onClick = { viewModel.setDialogShow(false) },
                                        shape = RoundedCornerShape(largeShape),
                                        colors = ButtonDefaults.buttonColors( backgroundColor = mainColor ),
                                    ) {
                                        Text( text = stringResource(id = R.string.alertOkay), color = Color.White, modifier = Modifier.padding(all = TextTopSmall))
                                    }
                                }
                            }
                        )

                    // Сообщение об отсутствие интернета
                    if (viewModel.dialogShow && !viewModel.internet)
                        NoInternet(onDismissRequest = {viewModel.setDialogShow(false)}, btnOnClick = {viewModel.setDialogShow(false)})

                    // Всплывающие сообщения
                    if (viewModel.isUpdationToastShow)
                        mToast(LocalContext.current, stringResource(id = R.string.editorGoBackError))

                    // Всплывающие сообщения
                    if (viewModel.isMaxImageCountToast)
                        mToast(LocalContext.current, stringResource(id = R.string.editorMaxCountError))
                }

                // Пробел
                Spacer(modifier = Modifier.padding(bottom = ScreenArea))
            }
        }
    )

}

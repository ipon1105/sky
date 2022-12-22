package com.example.sky.android.screens.main


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sky.android.R
import com.example.sky.android.composables.MyCircle
import com.example.sky.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import java.util.*
import androidx.compose.runtime.Composable
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.sky.android.models.FlatInfoViewModel
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import java.time.DayOfWeek
import java.time.YearMonth



@OptIn(ExperimentalPagerApi::class, ExperimentalCoilApi::class)
@Composable
fun MoreDetailBlock(viewModel: FlatInfoViewModel){

    // Из одной квартиры выносит список квартир, которые собирается отображать (в виде ссылок для скачивания)
    var imageList = viewModel.flat.photos
    if (imageList.size == 0)
        imageList += "no_image"

    val pagerState = rememberPagerState(initialPage = 0)
    val maxHorizontalPagerHeight = 200.dp
    val dropDownImageRotateAngle = 180.0f
    val dropDownImageSize = 45.dp
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(largeShape)
            )
            .padding(ComponentDiffNormal),
    ) {
        // Слайдер изображений
        HorizontalPager(count = imageList.size, state = pagerState, modifier = Modifier.height(height = maxHorizontalPagerHeight)){ page ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ComponentDiffLarge),
                shape = RoundedCornerShape(canvasShape),
            ) {

                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.DarkGray)){

                    // Модель загружается
                    if (viewModel.isFlatLoading)
                        CircularProgressIndicator(modifier = Modifier.fillMaxSize(), color = mainColor)

                    // У модели нет изображений
                    else if (imageList[0].equals("no_image"))
                        // Изображение по умолчаниюы
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            imageVector = ImageVector.vectorResource(id = R.drawable.no_image),
                            contentDescription = stringResource(id = R.string.imageDescriptionFlatPhoto),
                            colorFilter = ColorFilter.tint(Color.White),
                        )

                    // У модели есть свои изобржения
                    else
                        // Изображение из сети
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = rememberImagePainter(imageList[page]),
                            contentDescription = stringResource(id = R.string.imageDescriptionFlatPhoto),
                        )
                }
            }
        }

        // Индикатор
        HorizontalPagerIndicator(pagerState = pagerState, modifier = Modifier
            .padding(top = verticalNormal)
            .align(Alignment.CenterHorizontally)
        )

        Column(modifier = Modifier.animateContentSize()) {

            // Строка для раскрывающегося блока
            Row(modifier = Modifier.fillMaxWidth()) {
                // Текст
                ClickableText(
                    text = AnnotatedString(text = stringResource(id = R.string.flatInfoMoreDetails)) ,
                    onClick = { viewModel.setShowInfoBlock(!viewModel.showInfoBlock) },
                    style = TextStyle(
                        color = HeaderMainColorMain,
                        fontSize = LargeFont
                    ),
                )

                // Стрелочка
                Image(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = stringResource(id = R.string.imageDescriptionArrowDropDown),
                    modifier = Modifier
                        .graphicsLayer {
                            if (viewModel.showInfoBlock) rotationZ = dropDownImageRotateAngle
                        }
                        .size(dropDownImageSize)
                        .align(Alignment.CenterVertically)
                        .clickable { viewModel.setShowInfoBlock(!viewModel.showInfoBlock) },
                    colorFilter = ColorFilter.tint(color = ImageDropDownColorGray),
                )
            }

            // Скрытый блок
            if (viewModel.showInfoBlock){
                // Адрес
                Text(
                    text = stringResource(id = R.string.address) + ":",
                    color = GrayTextColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = ComponentDiffSmall),
                )
                Text(
                    text = viewModel.flat.address,
                    color = GrayTextColor,
                    modifier = Modifier.padding(top = ComponentDiffSmallSmall),
                )

                // Описание
                Text(
                    text = stringResource(id = R.string.description) + ":",
                    color = GrayTextColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = ComponentDiffSmall),
                )
                Text(
                    text = viewModel.flat.description,
                    color = GrayTextColor,
                    modifier = Modifier.padding(top = ComponentDiffSmallSmall),
                )

                // TODO: Сделать динамическое определение состояния квартиры и подсказки
                // Статус квартиры
                Row(modifier = Modifier.padding(top = ComponentDiffSmall)) {
                    // Состояние квартиры
                    MyCircle(
                        color = if (viewModel.flat.status == 0) FlatGreen else if (viewModel.flat.status == 1) FlatYellow else FlatRed,
                        size = canvasShape
                    )

                    // Текст подсказка
                    Text(
                        text = "Временный текст подсказка",
                        color = GrayTextColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = ComponentDiffSmallSmall)
                    )
                }

            }
        }


    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarBlock(){
    Column(
        modifier = Modifier
            .padding(top = ComponentDiffLarge)
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(largeShape)
            ),
    ) {
        //TODO: Сделать русификацию
        SelectableCalendar(
            firstDayOfWeek = DayOfWeek.MONDAY,
            calendarState = rememberSelectableCalendarState(
                initialMonth = YearMonth.now(),
                initialSelectionMode = SelectionMode.Period,
            ),
            modifier = Modifier.padding(ScreenArea),
        )


    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FlatInfoScreen(navController: NavHostController, flatId: String = ""){
    val viewModel = FlatInfoViewModel(flatId)

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
                        .clickable { viewModel.btnBackClick(navController) },
                    colorFilter = ColorFilter.tint(color = HeaderMainColorMain),
                )

                // Кнопки управления
                Box(){
                    // Кнопка настройки
                    Image(
                        imageVector = Icons.Rounded.Menu,
                        contentDescription = stringResource(id = R.string.imageDescriptionBack),
                        modifier = Modifier
                            .padding(all = ComponentDiffNormal)
                            .clickable { viewModel.setExpanded(!viewModel.expanded) },
                        colorFilter = ColorFilter.tint(color = HeaderMainColorMain),
                    )

                    // Выпадающее меню
                    DropdownMenu(
                        expanded = viewModel.expanded,
                        onDismissRequest = { viewModel.setExpanded(false) }
                    ) {
                        DropdownMenuItem(onClick = { viewModel.btnEditClick(navController) }) {
                            Text(text = stringResource(id = R.string.edit))
                        }
                        DropdownMenuItem(onClick = { viewModel.btnDeleteClick()  }) {
                            Text(text = stringResource(id = R.string.delete))
                        }
                    }
                }
            }
        },
        content = {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = ScreenArea)
                    .padding(top = it.calculateTopPadding())
                    .verticalScroll(rememberScrollState()),
            ) {
                // Пробел
                Spacer(modifier = Modifier.padding(top = ScreenArea))

                // TODO: Сделать добавление заселений
                // TODO: Сделать разные заселения разными цветами
                // TODO: Сделать смену состояния квартиры
                // TODO: Сделать редакирования заселений
                // TODO: Сделать инфомарцию об уборке
                // TODO: Сделать бронь
                // TODO: Сделать редактирование брони

                // Первый блок с инфомрацией о квартире
                MoreDetailBlock(viewModel)

                // Второй блок с календарём
                CalendarBlock()

                // Пробел
                Spacer(modifier = Modifier.padding(bottom = ScreenArea))
            }

            // Дейсвие во время удаления
            if (viewModel.showDialog && viewModel.isDeleting)
                AlertDialog(
                    onDismissRequest = { viewModel.setShowDialog(false) },
                    title = { Text(text = stringResource(id = R.string.error), color = Color.Red) },
                    text = { Text( text = stringResource(id = R.string.deletingError) ) },
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

            // Подтверждение о удалени квартиры
            if (viewModel.showConfirmDialog)
                AlertDialog(
                    onDismissRequest = { viewModel.setShowConfirmDialog(false) },
                    title = { Text(text = stringResource(id = R.string.warning), color = FlatYellow) },
                    text = { Text( text = stringResource(id = R.string.deletingWarning) ) },
                    buttons = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = alertDialogArea)
                                .padding(top = 0.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = { viewModel.setShowConfirmDialog(false); viewModel.deleting(navController) },
                                shape = RoundedCornerShape(largeShape),
                                colors = ButtonDefaults.buttonColors( backgroundColor = mainColor ),
                            ) {
                                Text( text = stringResource(id = R.string.alertYes), color = Color.White, modifier = Modifier.padding(all = TextTopSmall))
                            }
                            Button(
                                onClick = { viewModel.setShowConfirmDialog(false) },
                                shape = RoundedCornerShape(largeShape),
                                colors = ButtonDefaults.buttonColors( backgroundColor = mainColor ),
                            ) {
                                Text( text = stringResource(id = R.string.alertNo), color = Color.White, modifier = Modifier.padding(all = TextTopSmall))
                            }
                        }
                    }
                )
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPagerApi::class)
@Composable
@Preview(showBackground = true, backgroundColor = 0x00FFFFFF)
fun myPreview(){
    FlatInfoScreen(navController = rememberNavController())
}
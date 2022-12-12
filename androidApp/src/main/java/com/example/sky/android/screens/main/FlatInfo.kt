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
import com.example.sky.android.models.FlatModel
import com.example.sky.android.models.flatList
import com.example.sky.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import java.util.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import java.time.DayOfWeek
import java.time.YearMonth

@OptIn(ExperimentalPagerApi::class)
@Composable
fun moreDetailBlock(flat: FlatModel){

//    GlideImage()
    // Из одной квартиры выносит список квартир, которые собирается отображать (в виде ссылок для скачивания)
    val imageList = listOf(
        "Photo_1",
        "Photo_2",
        "Photo_3",
        "Photo_4",
        "Photo_5",
    )
    val pagerState = rememberPagerState(initialPage = 0)
    val maxHorizontalPagerHeight = 200.dp
    val dropDownImageRotateAngle = 180.0f
    val dropDownImageSize = 45.dp
    var showInfoBlock by remember{mutableStateOf(false)}
    
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
                val newImgae = imageList[page]
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.DarkGray)){
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
                    onClick = { showInfoBlock = !showInfoBlock },
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
                        .graphicsLayer { if (showInfoBlock) rotationZ = dropDownImageRotateAngle }
                        .size(dropDownImageSize)
                        .align(Alignment.CenterVertically)
                        .clickable { showInfoBlock = !showInfoBlock },
                    colorFilter = ColorFilter.tint(color = ImageDropDownColorGray),
                )
            }

            // Скрытый блок
            if (showInfoBlock){
                // Адрес
                Text(
                    text = stringResource(id = R.string.address) + ":",
                    color = GrayTextColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = ComponentDiffSmall),
                )
                Text(
                    text = flat.address,
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
                    text = flat.description,
                    color = GrayTextColor,
                    modifier = Modifier.padding(top = ComponentDiffSmallSmall),
                )

                // TODO: Сделать динамическое определение состояния квартиры и подсказки
                // Статус квартиры
                Row(modifier = Modifier.padding(top = ComponentDiffSmall),) {
                    // Состояние квартиры
                    MyCircle(color = FlatYellow, size = canvasShape)

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
fun calendarBlock(){
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
@OptIn(ExperimentalPagerApi::class)
@Composable
fun FlatInfoScreen(navController: NavHostController){
    var expanded by remember { mutableStateOf(false) }

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

                // Кнопки управления
                Box(){
                    // Кнопка настройки
                    Image(
                        imageVector = Icons.Rounded.Menu,
                        contentDescription = stringResource(id = R.string.imageDescriptionBack),
                        modifier = Modifier.padding(all = ComponentDiffNormal).clickable { expanded = !expanded },
                        colorFilter = ColorFilter.tint(color = HeaderMainColorMain),
                    )

                    // Выпадающее меню
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(onClick = { }) {
                            Text("Редактировать")
                        }
                        DropdownMenuItem(onClick = { }) {
                            Text("Удалить")
                        }
                        Divider()
                        DropdownMenuItem(onClick = { }) {
                            Text("Поделиться")
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

                // Первый блок с инфомрацией о квартире
                flatList[0]?.let { it1 -> moreDetailBlock(flat = it1) }

                // Второй блок с календарём
                calendarBlock()

                // Пробел
                Spacer(modifier = Modifier.padding(bottom = ScreenArea))
            }
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
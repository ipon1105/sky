package com.example.sky.android.screens.main


import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.sky.android.R
import com.example.sky.android.composables.MyCircle
import com.example.sky.android.composables.mToast
import com.example.sky.android.composables.ui.*
import com.example.sky.android.models.DealViewModel
import com.example.sky.android.models.FlatInfoViewModel
import com.example.sky.android.models.data.Deal
import com.example.sky.android.utils.connection.getDateTime
import com.example.sky.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.*
import java.util.*

@OptIn(ExperimentalPagerApi::class, ExperimentalCoilApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun MoreDetailBlock(viewModel: FlatInfoViewModel){

    // Из одной квартиры выносит список квартир, которые собирается отображать (в виде ссылок для скачивания)
    var imageList: List<Uri?> = viewModel.downloadImageList
    if (imageList.isEmpty())
        imageList = imageList.plus(null)

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
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = mainColor)

                    // У модели нет изображений
                    else if (imageList[0] == null)
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
                        GlideImage(
                            modifier = Modifier.fillMaxSize(),
                            model = imageList[page],
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
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    text = AnnotatedString(text = stringResource(id = R.string.flatInfoMoreDetails)) ,
                    onClick = { viewModel.setShowInfoBlock(!viewModel.showInfoBlock) },
                    style = TextStyle(
                        color = HeaderMainColorMain,
                        fontSize = NormalLargeFont
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
                        .clickable { viewModel.setShowInfoBlock(!viewModel.showInfoBlock) }
                        .weight(1f),
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

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnrememberedMutableState")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarBlock(viewModel: FlatInfoViewModel){
    val specViewModel = viewModel<DealViewModel>()
    if (viewModel.flat.detail.isNotEmpty())
        specViewModel.start(viewModel.flat.detail, LocalContext.current)

    Column(
        modifier = Modifier
            .padding(top = ComponentDiffLarge)
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(largeShape)
            ),
    ) {
        val calendarState = rememberSelectableCalendarState(
            initialMonth = YearMonth.now(),
            initialSelectionMode = SelectionMode.Period,
        )

        // Календарь
        SelectableCalendar(
            firstDayOfWeek = DayOfWeek.MONDAY,
            calendarState = calendarState,
            modifier = Modifier.padding(ScreenArea),
            dayContent = { dayState ->
                val cardSize = 50.dp
                val circleSize = 30.dp
                val circlePadding = 3.dp
                val cardPadding = 3.dp
                var cardElevation = 0.dp
                if (dayState.isFromCurrentMonth)
                    cardElevation = 2.dp
                var color = Color.White

                val date = dayState.date
                val selectionState = dayState.selectionState
                val isSelected = selectionState.isDateSelected(date)
                val today = getDateTime(SimpleDateFormat("yyyy-MM-dd").parse(date.toString()).time)

                // Разукрашиваем сделки
                val deal: Deal? = specViewModel.isBetween(today)
                if (deal != null)
                    color = Color(deal.color)

                Card(
                    modifier = Modifier
                        .size(cardSize)
                        .background(color = Color.White)
                        .padding(cardPadding),
                    elevation = cardElevation,
                    shape = RoundedCornerShape(shortShape),
                    onClick = {
                        selectionState.onDateSelected(date)
                        if ( selectionState.selection.isNotEmpty()) {
                            Log.d("viewModel", "ABX")
                            val tmpDeal = specViewModel.isInside(
                                getDateTime(SimpleDateFormat("yyyy-MM-dd").parse(selectionState.selection.first().toString()).time),
                                getDateTime(SimpleDateFormat("yyyy-MM-dd").parse(selectionState.selection.last().toString()).time)
                            )

                            if (tmpDeal != null)
                                specViewModel.setSelectedDeal(true, tmpDeal)
                            else
                                specViewModel.setSelectedDeal(false, null)
                        }
                        if (deal != null)
                            specViewModel.setSelectedDeal(true, deal)
                        else
                            specViewModel.setSelectedDeal(false, null)
                    },
                    backgroundColor = if (isSelected) mainColor else Color.White
                ) {

                    Box(
                        modifier = Modifier
                            .size(circleSize)
                            .padding(circlePadding)
                            .background(color = color, shape = CircleShape)
                    ){

                        if (dayState.isCurrentDay){
                            Text(text = "now", modifier = Modifier.align(Alignment.Center))
                        } else
                            Text(text = dayState.date.dayOfMonth.toString(), modifier = Modifier.align(Alignment.Center))
                    }
                }

            }
        )

        if (calendarState.selectionState.selection.isNotEmpty())
            AddDealBlock(viewModel = specViewModel, dateList = calendarState.selectionState.selection)

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FlatInfoScreen(navController: NavHostController, flatId: String = ""){
    val viewModel = viewModel<FlatInfoViewModel>()
    viewModel.start(flatId, context = LocalContext.current)

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

                if (viewModel.status == 2)
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
                CalendarBlock(viewModel)

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

@OptIn(ExperimentalGlideComposeApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun AddDealBlock(viewModel: DealViewModel, dateList: List<LocalDate>){

    // Главное содержимое блока
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = ComponentDiffNormal)
            .animateContentSize()
    ) {

        // Дата заселения
        Row(
            modifier = Modifier
                .padding(vertical = ComponentDiffSmall)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        )
        {
            TitleText(text = stringResource(id = R.string.check_in_date) + ": ")
            MainText(
                text =
                if (viewModel.isSelectedDeal)
                    GetTimeString(viewModel.dateIn)
                else
                    dateList.first().toString())
        }

        // Дата выселения
        Row(
            modifier = Modifier
                .padding(vertical = ComponentDiffSmall)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        )
        {
            TitleText(text = stringResource(id = R.string.check_out_date) + ": ")
            MainText(text =
            if (viewModel.isSelectedDeal)
                GetTimeString(viewModel.dateOut)
            else
                dateList.last().toString())
        }

        if (!viewModel.isSelectedDeal)
        {
            // Стоимость проживания в день
            VerticalCustomTextField(
                title = stringResource(id = R.string.pricePerDay),
                text = viewModel.price,
                placeholderText = stringResource(id = R.string.pricePerDay),
                onValueChange = { viewModel.setPrice(it) },
                fontSize = 14.sp,
            )
        } else
        {
            // Стоимость проживания в день
            Row(
                modifier = Modifier
                    .padding(vertical = ComponentDiffSmall)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            )
            {
                TitleText(text = stringResource(id = R.string.pricePerDay) + ": ")
                MainText(text = viewModel.price)
            }
        }

        if (!viewModel.isSelectedDeal)
            Row(modifier = Modifier.fillMaxWidth()) {
            val boxSize = 30.dp
            val boxSelectedSize = 45.dp

            val yellow = FlatYellow
            val red = FlatRed
            val green = FlatGreen

            Box(modifier = Modifier
                .padding(horizontal = ComponentDiffSmall)
                .size(if (viewModel.colorIndex == 0) boxSelectedSize else boxSize)
                .background(color = yellow, shape = CircleShape)
                .clickable { viewModel.setColorIndex(0) })
            Box(modifier = Modifier
                .padding(horizontal = ComponentDiffSmall)
                .size(if (viewModel.colorIndex == 1) boxSelectedSize else boxSize)
                .background(color = red, shape = CircleShape)
                .clickable { viewModel.setColorIndex(1) })
            Box(modifier = Modifier
                .padding(horizontal = ComponentDiffSmall)
                .size(if (viewModel.colorIndex == 2) boxSelectedSize else boxSize)
                .background(color = green, shape = CircleShape)
                .clickable { viewModel.setColorIndex(2) })
//            Box(modifier = Modifier.padding(horizontal = ComponentDiffSmall).size(if (viewModel.colorIndex == 3) boxSelectedSize else boxSize).background(color = blue, shape = CircleShape)  .clickable { viewModel.setColorIndex(3) })
//            Box(modifier = Modifier.padding(horizontal = ComponentDiffSmall).size(if (viewModel.colorIndex == 4) boxSelectedSize else boxSize).background(color = cyan, shape = CircleShape)  .clickable { viewModel.setColorIndex(4) })
        }
        
        // Текст Заголовок
        Text(
            text = stringResource(id = R.string.flatInfoClientInfo),
            style = TextStyle(
                color = HeaderMainColorMain,
                fontSize = LargeFont
            ),
        )

        val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){ uri ->
            Log.d("viewModel", uri.toString())
            if (uri != null) {
                viewModel.addLocalImageUri(uri)
            }
        }

        
        // Блок с полями личной информации
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier
                .fillMaxWidth(0.4f)
                .padding(top = ComponentDiffLarge)) {

                // Изображение по умолчаниюы
                if (viewModel.imageLocalUri == Uri.EMPTY && viewModel.imageLoadUri == Uri.EMPTY)
                    Image(
                        modifier = Modifier
                            .size(150.dp)
                            .padding(ComponentDiffNormal),
                        imageVector = ImageVector.vectorResource(id = R.drawable.no_image),
                        contentDescription = stringResource(id = R.string.imageDescriptionFlatPhoto),
                        colorFilter = ColorFilter.tint(Color.DarkGray),
                    )
                
                else 
                    GlideImage(model = if (viewModel.imageLocalUri == Uri.EMPTY) viewModel.imageLoadUri else viewModel.imageLocalUri, contentDescription = "")

                // Вспомогательная панель
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    // Изображение для Добавления
                    Image(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.imageDescriptionAdd),
                        colorFilter = ColorFilter.tint(FlatGreen),
                        modifier = Modifier
                            .size(iconSizeNormal)
                            .clickable { launcher.launch("image/*")/*TODO: Сделать добавление изображения*/ },
                    )
                }
            }


            // Поля для заполнения
            Column(modifier = Modifier.fillMaxWidth()) {
                // ФИО
                VerticalCustomTextField(
                    title = stringResource(id = R.string.fio),
                    text = viewModel.fio,
                    placeholderText = stringResource(id = R.string.fio),
                    onValueChange = { viewModel.setFio(it) },
                    fontSize = 14.sp,
                )

                // Телефон
                VerticalCustomTextField(
                    title = stringResource(id = R.string.phone),
                    text = viewModel.phone,
                    placeholderText = stringResource(id = R.string.phone),
                    onValueChange = { viewModel.setPhone(it) },
                    fontSize = 14.sp,
                )

                // Паспорт
                VerticalCustomTextField(
                    title = stringResource(id = R.string.passport),
                    text = viewModel.passport,
                    placeholderText = stringResource(id = R.string.passportSN),
                    onValueChange = { viewModel.setPassport(it) },
                    fontSize = 14.sp,
                )
            }
        }

        // Прописка
        VerticalCustomTextField(
            title = stringResource(id = R.string.registration),
            text = viewModel.registration,
            placeholderText = stringResource(id = R.string.registration),
            onValueChange = { viewModel.setRegistration(it) },
            fontSize = 14.sp,
        )

        // Кнопка Добавить
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ComponentDiffNormal),
            horizontalArrangement = Arrangement.Center
        ){
            ProgressButton(
                text = if (viewModel.isSelectedDeal) stringResource(id = R.string.edit) else stringResource(id = R.string.add),
                onClick = { viewModel.btnApplyClick(
                    Timestamp(SimpleDateFormat("yyyy-MM-dd").parse(dateList.first().toString()).time),
                    Timestamp(SimpleDateFormat("yyyy-MM-dd").parse(dateList.last().toString()).time)
                ) },
                isLoading = viewModel.isClientInfoUpdating || viewModel.isNewDealCreating,
                btnColor = if (viewModel.isSelectedDeal) FlatYellow else FlatGreen
            )
        }

        if (viewModel.priceToast)
            mToast(LocalContext.current, "Невозможно создать сделку без цены")
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun GetTimeString(l: Long):String{
    val triggerTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(l),
        TimeZone.getDefault().toZoneId()
    )

    return triggerTime.toString()
}

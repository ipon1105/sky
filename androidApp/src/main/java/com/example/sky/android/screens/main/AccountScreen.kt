package com.example.sky.android.screens

import com.example.sky.android.R
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.sky.android.composables.ui.infoText
import com.example.sky.android.composables.ui.mainText
import com.example.sky.android.composables.ui.titleText
import com.example.sky.android.models.AccountViewModel
import com.example.sky.android.models.data.UserData
import com.example.sky.navigation.NavRoute
import com.example.sky.ui.theme.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun AccountScreen(navController: NavHostController) {
    val viewModel = viewModel<AccountViewModel>()

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
                horizontalArrangement = Arrangement.End
            ){
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
                        DropdownMenuItem(onClick = { navController.navigate(NavRoute.SettingInfo.route)}) {
                            Text(text = stringResource(id = R.string.setting))
                        }
                        DropdownMenuItem(onClick = { Firebase.auth.signOut(); navController.navigate(NavRoute.Login.route){popUpTo(0)} }) {
                            Text(text = stringResource(id = R.string.logout))
                        }
                    }
                }
                
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
                    AccountCard(viewModel.userData,viewModel, navController)
                }

                // Пробел
                Spacer(modifier = Modifier.padding(bottom = ScreenArea))
            }
        }
    )

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AccountCard(data: UserData, viewModel: AccountViewModel, navController: NavHostController){
    val cardElevation = 5.dp
    val photoSize = 130.dp
    val status = 40.dp

    Card(
        shape = RoundedCornerShape(size = largeShape),
        modifier = Modifier.fillMaxWidth(), elevation = cardElevation){
        Column(modifier = Modifier.fillMaxWidth()) {
            // Первая половина
            Row(modifier = Modifier.fillMaxWidth()) {
                // Изображение по умолчанию
                if (data.photo.isEmpty()) {
                    Image(
                        modifier = Modifier
                            .size(photoSize)
                            .background(color = DarkGray),
                        imageVector = ImageVector.vectorResource(id = R.drawable.no_image),
                        contentDescription = stringResource(id = R.string.imageDescriptionFlatPhoto),
                        colorFilter = ColorFilter.tint(Color.White),
                    )
                    // Изображение из сети
                }else {
                    // TODO: Сделать загрузку из сети
                    GlideImage(
                        modifier = Modifier
                            .size(photoSize)
                            .background(color = DarkGray),
                        model = data.photo,
                        contentDescription = stringResource(id = R.string.imageDescriptionFlatPhoto),
                    )
                }


                //Личная информация
                Column(modifier = Modifier.padding(start = ComponentDiffSmall)) {

                    Row(modifier = Modifier.fillMaxWidth()){
                        titleText(
                            text = data.surname,
                            fontSize = 26.sp,
                            modifier = Modifier
                                .weight(9f)
                                .padding(top = ComponentDiffSmall),
                        )

                        //Статус
//                        Box(
//                            modifier = Modifier
//                                .size(status)
//                                .background(
//                                    shape = RoundedCornerShape(bottomStart = shortShape),
//                                    color = DarkGray
//                                )
//                                .weight(2f)
//                                .clickable { viewModel.editAccount(navController) }
//                        ){
//                            Icon(modifier = Modifier.fillMaxSize().padding(ComponentDiffSmall),imageVector = Icons.Filled.Edit, contentDescription = "", tint = Color.White)
//                        }
                    }


                    Row() {
                        mainText(text = data.name, fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(5.dp))
                        mainText(text = data.patronymic, fontSize = 18.sp)
                    }

                    Text(modifier = Modifier.padding(top = ComponentDiffNormal),text = if (data.status == 2) "Администратор" else "Работник", fontSize =  28.sp, color = mainColor)
                }
            }

            // Заголовок
            mainText(modifier = Modifier.padding(start = ComponentDiffNormal), text = "Контактные данные", fontSize = 25.sp)

            // Телефон
            Row(
                modifier = Modifier
                    .padding(top = ComponentDiffSmall, start = ComponentDiffNormal)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            )
            {
                titleText(text = "Телефон: ")
                mainText(text =  "${data.telephone}")
            }

            // Почта
            Row(
                modifier = Modifier
                    .padding(vertical = ComponentDiffSmall)
                    .padding(start = ComponentDiffNormal)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            )
            {
                titleText(text = "Почта: ")
                mainText(text =  "${Firebase.auth.currentUser?.email}")
            }
        }
    }
}
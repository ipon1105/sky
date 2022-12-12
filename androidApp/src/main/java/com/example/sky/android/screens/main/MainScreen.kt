package com.example.sky.android.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.sky.android.R
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sky.ui.theme.*

@Composable
fun MainScreen(navController: NavHostController)
{
    val bottomState = remember{mutableStateOf("List")}

    //ImageVector.vectorResource(R.drawable.)
    Scaffold(
        backgroundColor = HeaderMainColorMain,
        drawerShape =  RoundedCornerShape(
            topStart = bottomBarCornerShape,
            topEnd = bottomBarCornerShape
        ),
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(
                            topStart = bottomBarCornerShape,
                            topEnd = bottomBarCornerShape
                        )
                    ),
            ){
                BottomNavigation(
                    backgroundColor = Color.Transparent,
                    modifier = Modifier.background(
                        shape = RoundedCornerShape(
                            topStart = bottomBarCornerShape,
                            topEnd = bottomBarCornerShape),
                        color = Color.White
                    ),
                    elevation = 0.dp
                ){
                    //TODO: Слегка увеличить размер иконок
                    BottomNavigationItem(
                        selected = bottomState.value == "List",
                        onClick = { bottomState.value = "List"},
                        label = { Text(text = stringResource(id = R.string.list), color = Color.Black)},
                        //TODO: Иконка не из того сета. Взять одинаковые иконки
                        icon = { Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = stringResource(id = R.string.imageDescriptionList),
                            tint = if (bottomState.value == "List") HeaderMainColorMain else DarkGray
                        )},
                    )
                    BottomNavigationItem(
                        selected = bottomState.value == "Map",
                        onClick = { bottomState.value = "Map"},
                        label = { Text(text = stringResource(id = R.string.map), color = Color.Black)},
                        icon = { Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = stringResource(id = R.string.imageDescriptionMap),
                            tint = if (bottomState.value == "Map") HeaderMainColorMain else DarkGray
                        )},
                    )
                    BottomNavigationItem(
                        selected = bottomState.value == "Account",
                        onClick = { bottomState.value = "Account"},
                        label = { Text(text = stringResource(id = R.string.account), color = Color.Black)},
                        icon = { Icon(
                            imageVector = Icons.Filled.AccountBox,
                            contentDescription = stringResource(id = R.string.imageDescriptionAccount),
                            tint = if (bottomState.value == "Account") HeaderMainColorMain else DarkGray
                        )},
                    )
                }
            }

        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = it.calculateBottomPadding())
                    .background(color = HeaderMainColorMain)
            ) {
                when(bottomState.value){
                    "List" -> ListScreen(navController)
                    "Map" -> MapScreen()
                    "Account" -> AccountScreen(navController)
                }
            }
        }
    )
}

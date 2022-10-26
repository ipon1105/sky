package com.example.sky.android.screens

import androidx.compose.foundation.layout.*
import com.example.sky.android.R
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun MainScreen(navController: NavHostController)
{
    val btnBackColor = Color(red = 0x00, green = 0x71, blue = 0xBC)
    val bottomState = remember{mutableStateOf("List")}

    //ImageVector.vectorResource(R.drawable.)
    Scaffold(
        topBar = {

        },
        bottomBar = {
            BottomNavigation(backgroundColor = btnBackColor) {
                BottomNavigationItem(
                    selected = bottomState.value == "List",
                    onClick = { bottomState.value = "List"},
                    label = { Text(text = "List")},
                    icon = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.list), contentDescription = "List Icon")},
                )
                BottomNavigationItem(
                    selected = bottomState.value == "Map",
                    onClick = { bottomState.value = "Map"},
                    label = { Text(text = "Map")},
                    icon = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.map), contentDescription = "Map Icon")},
                )
                BottomNavigationItem(
                    selected = bottomState.value == "Account",
                    onClick = { bottomState.value = "Account"},
                    label = { Text(text = "Account")},
                    icon = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.account), contentDescription = "Account Icon")},
                )
            }
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .padding(bottom = it.calculateBottomPadding())
            ) {
                when(bottomState.value){
                    "List" -> ListScreen()
                    "Map" -> MapScreen()
                    "Account" -> AccountScreen()
                }
            }
        }
    )
}

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
import androidx.navigation.NavHostController
import com.example.sky.ui.theme.mainColor

@Composable
fun MainScreen(navController: NavHostController)
{
    val bottomState = remember{mutableStateOf("List")}

    //ImageVector.vectorResource(R.drawable.)
    Scaffold(
        topBar = {

        },
        bottomBar = {
            BottomNavigation(backgroundColor = mainColor) {
                BottomNavigationItem(
                    selected = bottomState.value == "List",
                    onClick = { bottomState.value = "List"},
                    label = { Text(text = "List", color = Color.White)},
                    icon = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.list), contentDescription = "List Icon", tint = Color.White)},
                )
                BottomNavigationItem(
                    selected = bottomState.value == "Map",
                    onClick = { bottomState.value = "Map"},
                    label = { Text(text = "Map", color = Color.White)},
                    icon = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.map), contentDescription = "Map Icon", tint = Color.White)},
                )
                BottomNavigationItem(
                    selected = bottomState.value == "Account",
                    onClick = { bottomState.value = "Account"},
                    label = { Text(text = "Account", color = Color.White)},
                    icon = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.account), contentDescription = "Account Icon", tint = Color.White)},
                )
            }
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
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

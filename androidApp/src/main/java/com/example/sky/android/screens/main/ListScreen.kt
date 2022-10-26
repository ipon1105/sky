package com.example.sky.android.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ListScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ){
            Button(onClick = { /*TODO: Сортировка Свободно*/ }) {
                Text("Свободно")
            }
            Button(onClick = { /*TODO: Сортировка Занято*/ }) {
                Text("Занято")
            }
            Button(onClick = { /*TODO: Сортировка Грязно*/ }) {
                Text("Грязно")
            }
        }
    }
}
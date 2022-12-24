package com.example.sky.android.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.sky.android.R
import com.example.sky.ui.theme.ComponentDiffNormal
import com.example.sky.ui.theme.LargeFont

@Composable
fun MapScreen() {
    // Заголовок страницы
    Text(
        text = stringResource(id = R.string.map_not_sup),
        color = Color.White,
        fontSize = LargeFont,
        modifier = Modifier.padding(ComponentDiffNormal)
    )
}
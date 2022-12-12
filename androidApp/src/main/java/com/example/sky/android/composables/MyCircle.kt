package com.example.sky.android.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun MyCircle(color: Color, size: Dp){
    Canvas(modifier = Modifier.size(size), onDraw = {
        drawCircle(color = color)
    })
}
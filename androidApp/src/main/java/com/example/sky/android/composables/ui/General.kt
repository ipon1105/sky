package com.example.sky.android.composables.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.sky.ui.theme.*

// Свой компонент TextField масштабируемый по высоте
@Composable
fun CustomTextField(
    text: String = "",
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    placeholderText: String = "Placeholder",
    fontSize: TextUnit = MaterialTheme.typography.body2.fontSize,
    onValueChange: (it: String)->Unit
) {
    BasicTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = text,
        onValueChange = onValueChange,
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colors.primary),
        textStyle = LocalTextStyle.current.copy(
            color = MaterialTheme.colors.onSurface,
            fontSize = fontSize
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier.background(color = lightGray,shape = RoundedCornerShape(shortShape),),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) leadingIcon()
                Box(
                    Modifier
                        .weight(1f)
                        .padding(2.dp)) {
                    if (text.isEmpty()) Text(
                        placeholderText,
                        style = LocalTextStyle.current.copy(
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                            fontSize = fontSize
                        )
                    )
                    innerTextField()
                }

                if (trailingIcon != null) trailingIcon()
            }
        }
    )

}

// Заголовок
@Composable
fun titleText(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = TextUnit.Unspecified
){
    Text(
        modifier = modifier,
        text = text,
        color = GrayTextColor,
        fontWeight = FontWeight.Bold,
        fontSize = fontSize,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

// Главное содержимое
@Composable
fun mainText(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = TextUnit.Unspecified,
){
    Text(
        modifier = modifier,
        text = text,
        color = GrayTextColor,
        fontSize = fontSize,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

// Блок с информацией справа от заголовка
@Composable
fun infoText(
    modifier: Modifier = Modifier,
    title: String,
    main: String
){
    Row(
        modifier = Modifier
            .padding(vertical = ComponentDiffSmall)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    )
    {
        titleText(text = title)
        mainText(text = main)
    }
}


// Блок с полем ввода и информацией о необходимом содержимом
@Composable
fun HorizontalCustomTextField(
    title: String = "",
    text: String = "",
    placeholderText: String = "Placeholder",
    fontSize: TextUnit = MaterialTheme.typography.body2.fontSize,
    onValueChange: (it: String)->Unit,
){
    val CUSTOM_TEXT_FILED_HEIGHT = 40.dp
    val CUSTOM_TEXT_FILED_PADDING = 4.dp

    Row(
        modifier = Modifier
            .padding(vertical = ComponentDiffSmall)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    )
    {
        titleText(text = title)
        CustomTextField(
            text = text,
            placeholderText = placeholderText,
            onValueChange = onValueChange,
            modifier = Modifier
                .background(
                    MaterialTheme.colors.surface,
                    RoundedCornerShape(percent = 50)
                )
                .padding(CUSTOM_TEXT_FILED_PADDING)
                .height(CUSTOM_TEXT_FILED_HEIGHT)
                .align(Alignment.CenterVertically),
            fontSize = fontSize,
        )
    }
}

// Блок с полем ввода и информацией о необходимом содержимом
@Composable
fun VerticalCustomTextField(
    title: String = "",
    text: String = "",
    placeholderText: String = "Placeholder",
    fontSize: TextUnit = MaterialTheme.typography.body2.fontSize,
    onValueChange: (it: String)->Unit,
){
    val CUSTOM_TEXT_FILED_HEIGHT = 40.dp

    Column(
        modifier = Modifier
            .padding(vertical = ComponentDiffSmall)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
    )
    {
        titleText(text = title)
        CustomTextField(
            text = text,
            placeholderText = placeholderText,
            onValueChange = onValueChange,
            modifier = Modifier
                .background(
                    MaterialTheme.colors.surface,
                    RoundedCornerShape(percent = 50)
                )
                .height(CUSTOM_TEXT_FILED_HEIGHT)
                .align(Alignment.Start),
            fontSize = fontSize,
        )
    }
}

// Кнопка с заменой на загрузку
@Composable
fun ProgressButton(
    text: String = "",
    onClick: () -> Unit,
    btnColor: Color = FlatGreen,
    textColor: Color = Color.White,
    progressColor: Color = mainColor,
    isLoading: Boolean = false,
){
    Button(
        enabled = !isLoading,
        shape = RoundedCornerShape(shortShape),
        colors = ButtonDefaults.buttonColors(backgroundColor = btnColor),
        onClick = onClick,
    ) {
        if (isLoading)
            CircularProgressIndicator(color = progressColor)
        else
            Text(text = text, color = textColor)
    }
}
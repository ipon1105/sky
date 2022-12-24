package com.example.sky.android.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.sky.android.R
import com.example.sky.ui.theme.*

@Composable
fun EditorTextField(
    hint: String = "",
    text: String = "",
    placeholder: String = "",
    onValueChange: (it: String) -> Unit,
    isError: Boolean = false,
    isClear: Boolean = false,
    maxLength: Int = 512,
    errorMsg: String = "",
    minHeight: Dp = 0.dp,
){
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(ComponentDiffNormal)){
        // Текст подсказка
        Text(
            text = hint,
            fontSize = SmallFont,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        // Поле ввода
        if (minHeight == 0.dp)
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                value = text,
                onValueChange = onValueChange,
                placeholder = { Text(text = placeholder, fontSize = SmallFont) },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.Black,
                    backgroundColor = lightGray,
                    disabledLabelColor = lightGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(shortShape),
                isError = isError,
                trailingIcon = {
                    if (text.isNotEmpty() && isClear)
                        IconButton(onClick = { onValueChange.invoke("") }) {
                            Icon(imageVector = Icons.Outlined.Close, contentDescription = stringResource(R.string.imageDescriptionClose))
                        }
                }
            )
        else
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .height(minHeight),
                value = text,
                onValueChange = onValueChange,
                placeholder = { Text(text = placeholder, fontSize = SmallFont) },
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.Black,
                    backgroundColor = lightGray,
                    disabledLabelColor = lightGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(shortShape),
                isError = isError,
                trailingIcon = {
                    if (text.isNotEmpty() && isClear)
                        IconButton(onClick = { onValueChange.invoke("") }) {
                            Icon(imageVector = Icons.Outlined.Close, contentDescription = stringResource(R.string.imageDescriptionClose))
                        }
                }
            )

        // Тексты под полем ввода
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = ComponentDiffSmallSmall)){

            val errorColor = if (isError) MaterialTheme.colors.error else Color.Transparent

            // Ошибка
            Text(
                text = errorMsg,
                color = errorColor,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .padding(start = ErrorStart)
                    .fillMaxWidth(0.75f),
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            // Количество допустимых символов
            Text(
                text = "${text.length} / ${maxLength}",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
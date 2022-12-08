package com.example.sky.android.screens

import androidx.compose.foundation.Canvas
import com.example.sky.android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sky.android.models.FlatModel
import com.example.sky.android.models.flatList
import com.example.sky.ui.theme.*

@Composable
fun ListScreen() {
    val search = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        // TODO: Добавить пояснение для кружочков в элементах списка
        // TODO: Оступ в элементе списка справа больше, чем слевва - Сделать одинаковыми.
        // TODO: Попробовать изменить кружочек на флажок, что бы была другая асоциация

        /*TODO: Сортировка Свободно*/
        /*TODO: Сортировка Занято*/
        /*TODO: Сортировка Грязно*/
        TextField(
            value = search.value,
            onValueChange = {
                search.value = it
            },
            placeholder = { Text(text = stringResource(id = R.string.search)) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(top = ComponentDiffNormal),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.LightGray,
                unfocusedIndicatorColor = Color.LightGray
            ),
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = stringResource(id = R.string.imageDescriptionEmail)) },
        )


        LazyColumn{
            itemsIndexed(
                items = flatList
            ){ index, item ->
                if (item.address.contains(search.value, ignoreCase = true))
                    FlatElementList(item)
            }
        }
    }
}

@Composable
fun FlatElementList(el: FlatModel){
    val cardElevation = 10.dp
    val cardSize = 100.dp

    // ModelView
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = verticalNormal),
        shape = RoundedCornerShape(size = largeShape),
        elevation = cardElevation
    ) {
        Row(
        ) {
            //Изображение квартиры
            Image(
                //TODO: Сделать загрузку изображений из базы данных firebase
                imageVector = ImageVector.vectorResource(id = R.drawable.map),
                contentDescription = stringResource(id = R.string.imageDescriptionFlatPhoto),
                modifier = Modifier
                    .size(cardSize)
                    .clip(RoundedCornerShape(size = largeShape))
                    .padding(vertical = verticalNormal),
            )
            
            Column(
                modifier = Modifier
                    .padding(all = ComponentAll10)
                    .padding(bottom = 0.dp),
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Название квартиры (улица)
                    Row(modifier = Modifier.fillMaxWidth(0.9f)){
                        Text(
                            text = el.address.uppercase(),
                            fontWeight = FontWeight.W700,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                    }

                    //Статус квартиры
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ){
                        MyCircle()
                    }
                }

                // Описание квартиры
                Text(
                    modifier = Modifier
                        .fillMaxHeight(),
                    maxLines = 3,
                    text = el.description,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = true
                )
            }
        }
    }
}

@Composable
fun MyCircle(){
    Canvas(modifier = Modifier.size(canvasShape), onDraw = {
        drawCircle(color = Color.Red)
    })
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
fun Preview(){
    ListScreen()
}
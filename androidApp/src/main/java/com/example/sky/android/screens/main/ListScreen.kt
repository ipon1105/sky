package com.example.sky.android.screens

import androidx.compose.foundation.Canvas
import com.example.sky.android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sky.android.models.FlatModel

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
//        Row(
//            horizontalArrangement = Arrangement.SpaceBetween,
//            modifier = Modifier.fillMaxWidth(),
//        ){
//            Button(onClick = { /*TODO: Сортировка Свободно*/ }) {
//                Text("Свободно")
//            }
//            Button(onClick = { /*TODO: Сортировка Занято*/ }) {
//                Text("Занято")
//            }
//            Button(onClick = { /*TODO: Сортировка Грязно*/ }) {
//                Text("Грязно")
//            }
//        }
        TextField(
            value = search.value,
            onValueChange = {
                search.value = it
            },
            placeholder = { Text(text = "Search") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(top = 12.dp),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.LightGray,
                unfocusedIndicatorColor = Color.LightGray
            ),
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Email icon") },
        )
        val list: ArrayList<FlatModel>
        //list.add(FlatModel(id = 1u, address = "Ул. Калинина, д. 102, кв. 10", photo = "photo1", owner = 1u, pricePerDay = 1u, description = "Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1"))

        LazyColumn{
            itemsIndexed(
                items = listOf(
                    FlatModel(id = 2u, address = "Ул. Тракторостроителей, д.52, кв. 17", photo = "photo2", owner = 2u, pricePerDay = 2u, description = "Description2"),
                    FlatModel(id = 3u, address = "address3", photo = "photo3", owner = 3u, pricePerDay = 3u, description = "Description3"),
                    FlatModel(id = 4u, address = "address4", photo = "photo4", owner = 4u, pricePerDay = 4u, description = "Description4"),
                    FlatModel(id = 5u, address = "address5", photo = "photo5", owner = 5u, pricePerDay = 5u, description = "Description5"),
                    FlatModel(id = 6u, address = "address6", photo = "photo6", owner = 6u, pricePerDay = 6u, description = "Description6"),
                    FlatModel(id = 7u, address = "address7", photo = "photo7", owner = 7u, pricePerDay = 7u, description = "Description7"),
                    FlatModel(id = 8u, address = "address8", photo = "photo8", owner = 8u, pricePerDay = 8u, description = "Description8"),
                    FlatModel(id = 9u, address = "address9", photo = "photo9", owner = 9u, pricePerDay = 9u, description = "Description9"),
                    FlatModel(id = 10u, address = "address10", photo = "photo10", owner = 10u, pricePerDay = 10u, description = "Description10"),
                    FlatModel(id = 11u, address = "address11", photo = "photo11", owner = 11u, pricePerDay = 11u, description = "Description11"),
                    FlatModel(id = 12u, address = "address12", photo = "photo12", owner = 12u, pricePerDay = 12u, description = "Description12"),
                    FlatModel(id = 13u, address = "address13", photo = "photo13", owner = 13u, pricePerDay = 13u, description = "Description13"),
                    FlatModel(id = 14u, address = "address14", photo = "photo14", owner = 14u, pricePerDay = 14u, description = "Description14"),
                    FlatModel(id = 15u, address = "address15", photo = "photo15", owner = 15u, pricePerDay = 15u, description = "Description15"),
                    FlatModel(id = 16u, address = "address16", photo = "photo16", owner = 16u, pricePerDay = 16u, description = "Description16"),
                    FlatModel(id = 17u, address = "address17", photo = "photo17", owner = 17u, pricePerDay = 17u, description = "Description17"),
                    FlatModel(id = 18u, address = "address18", photo = "photo18", owner = 18u, pricePerDay = 18u, description = "Description18"),
                    FlatModel(id = 19u, address = "address19", photo = "photo19", owner = 19u, pricePerDay = 19u, description = "Description19"),
                )
            ){ _, item ->
                FlatElementList(item)
            }
        }
    }
}

@Composable
fun FlatElementList(el: FlatModel){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 10.dp
    ) {
        Row(
        ) {
            Image(
                //TODO: Сделать загрузку изображений из базы данных firebase
                imageVector = ImageVector.vectorResource(id = R.drawable.map),
                contentDescription = "Flat Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .padding(vertical = 10.dp),
            )
            
            Column(
                modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp),
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(0.9f)){
                        Text(
                            text = el.address.uppercase(),
                            fontWeight = FontWeight.W700,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                    }

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ){
                        MyCircle()
                        //TODO: Нарисовать круг состояния
                    }
                }
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
    Canvas(modifier = Modifier.size(20.dp), onDraw = {
        drawCircle(color = Color.Red)
    })
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
fun preview(){
    ListScreen()
}
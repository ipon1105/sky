package com.example.sky.android.models

//Класс описывающий Квартиру в базе данных
data class FlatModel(
    val id: UInt,
    val address: String,
    val photo: String,
    val owner: UInt,
    var pricePerDay: UInt,
    val description: String
)

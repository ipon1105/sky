package com.example.sky.android.models

//Класс описывающий Работника в базе данных
data class WorkerModel(
    val id: UInt,
    val photo: String,
    val name: String,
    val surname: String,
    val patronymic: String,
    val description: String
)


package com.example.sky.android.models.data

// Общие данные между обычным работником и администратором
data class UserData (
    val name: String = "",
    val surname: String = "",
    val patronymic: String = "",
    val photo: String = "",
    val telephone: String = "",
    val status: Int = -1,
)

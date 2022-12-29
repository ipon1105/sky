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

// Возвращает объект с пометкой, что данные загружаются
fun getUserDataLoading():UserData{
    return UserData(
        name = "Имя загружается",
        surname = "Фамилия загружается",
        patronymic = "Отчество загружается",
        photo = "",
        telephone = "Загружается",
        status = -1,
    )
}
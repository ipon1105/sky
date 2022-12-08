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

// Тестовый список квартир
val flatList = listOf(
    FlatModel(id = 1u, address = "Ул. Калинина, д. 102, кв. 10", photo = "photo1", owner = 1u, pricePerDay = 1u, description = "Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1Description1"),
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
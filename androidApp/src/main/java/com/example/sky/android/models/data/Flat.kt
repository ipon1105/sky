package com.example.sky.android.models.data

data class Flat(
    var owner: String = "",
    var address: String = "",
    var description: String = "",
    val photos: List<String> = listOf(),
    var cleaningCost: String = "",
)
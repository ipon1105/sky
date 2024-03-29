package com.example.sky.android.models.data

data class Flat(
    var owner: String = "",
    var address: String = "",
    var description: String = "",
    var photos: List<String> = listOf(),
    var cleaningCost: String = "",
    var flatId: String = "",
    var status: Int = 0,
    var detail: String = "",
)
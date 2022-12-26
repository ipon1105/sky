package com.example.sky.android.models.data

data class Admin(
    val auth: String = "",
    val info: String = "",
    var flatList: List<String> = listOf(),
    var workerList: List<String> = listOf(),
)
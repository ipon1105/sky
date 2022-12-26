package com.example.sky.android.models.data

data class Worker(
    var auth: String = "",
    val info: String = "",
    val description: String = "",
    var adminList: List<String> = emptyList(),
    var notifyList: List<String> = emptyList(),
)

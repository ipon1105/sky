package com.example.sky.android.models

data class Worker(
    val auth: String = "",
    val info: String = "",
    val description: String = "",
    var adminList: List<String> = emptyList(),
)

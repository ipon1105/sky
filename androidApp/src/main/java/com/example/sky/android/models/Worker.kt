package com.example.sky.android.models

data class Worker(
    val auth: String = "",
    val info: String = "",
    val description: String = "",
    val adminList: List<String> = emptyList(),
)

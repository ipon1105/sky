package com.example.sky

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
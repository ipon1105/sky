package com.example.sky.android.utils.connection

import java.sql.Date
import java.text.SimpleDateFormat

fun getDateTime(long: Long): Date {
    try {
        return  Date(long * 1000)
    } catch (e: Exception) {
    }
    return Date(0)
}
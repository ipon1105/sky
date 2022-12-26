package com.example.sky.android.models.data

import java.sql.Timestamp

data class Deal(
    var color: Int = 0,
    var dateStart: Timestamp = Timestamp(1L),
    var dateStop: Timestamp = Timestamp(1L),
    var pricePerDay: String = "",
    var detail: String = "",
)

data class DealGot(
    var color: Int = 0,
    var dateStart: java.util.Date = Timestamp(1L),
    var dateStop: java.util.Date = Timestamp(1L),
    var pricePerDay: String = "",
    var detail: String = "",
)

fun DealGot_to_Deal(dealGot: DealGot) : Deal{
    return Deal(
        color = dealGot.color,
        dateStart = Timestamp(dealGot.dateStart.time),
        dateStop = Timestamp(dealGot.dateStop.time),
        pricePerDay = dealGot.pricePerDay,
        detail = dealGot.detail,
    )
}
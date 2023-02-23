package com.kerimbr.kotnews.core.utils.extensions

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun String.parseISODate(): String {
    val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
    val outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yy - HH:mm:ss")
    val dateTime = LocalDateTime.parse(this, inputFormatter)
    return dateTime.format(outputFormatter)
}
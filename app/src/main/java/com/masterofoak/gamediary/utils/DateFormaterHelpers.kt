package com.masterofoak.gamediary.utils

import android.content.Context
import android.text.format.DateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun getFormatedDate(timestamp: Long): String {
    val instant = Instant.ofEpochMilli(timestamp)
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()) // Use system's time zone
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yy", Locale.getDefault())
    return localDateTime.format(formatter)
}

fun getFormatedDateWithHours(timestamp: Long, context: Context): String {
    val instant = Instant.ofEpochMilli(timestamp)
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()) // Use system's time zone
    val is12HoursFormat = if (DateFormat.is24HourFormat(context)) "hh" else "HH"
    val formatter = DateTimeFormatter.ofPattern("$is12HoursFormat:mm dd.MM.yy", Locale.getDefault())
    return localDateTime.format(formatter)
}
package kz.iitu.diplom.crm.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.isToday(): Boolean {
    val f = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    return f.format(date) == f.format(Date())
}

fun String?.parse(): Date {
    val format = SimpleDateFormat("dd.MM.yyyy,HH:mm", Locale.getDefault())
    return format.parse(this) ?: Date()
}

fun Date.format(): String {
    val format = SimpleDateFormat("dd.MM.yyyy,HH:mm", Locale.getDefault())
    return format.format(this)
}
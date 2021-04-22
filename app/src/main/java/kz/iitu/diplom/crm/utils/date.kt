package kz.iitu.diplom.crm.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun Date.isToday(): Boolean {
    val f = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    return f.format(this) == f.format(Date())
}

fun String?.parse(): Date {
    val format = SimpleDateFormat("dd.MM.yyyy,HH:mm", Locale.getDefault())
    return try {
        format.parse(this) ?: Date()
    } catch (e: ParseException) {
        Date()
    }
}

fun Date.format(): String {
    val format = SimpleDateFormat("dd.MM.yyyy,HH:mm", Locale.getDefault())
    return format.format(this)
}

fun Date.formatLocal(): String {
    val format = SimpleDateFormat("dd MMMM HH:mm", Locale.getDefault())
    return format.format(this)
}
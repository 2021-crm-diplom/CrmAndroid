package kz.iitu.diplom.crm.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.isToday(): Boolean {
    val f = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    return f.format(date) == f.format(Date())
}
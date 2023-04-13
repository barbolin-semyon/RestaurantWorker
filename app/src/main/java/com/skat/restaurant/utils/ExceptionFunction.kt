package com.skat.restaurant.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Проверка строки на валидность email-адреса
 */
fun String.emailIfValid(): Boolean {
    return (this.isEmpty() || this.matches(Regex("\\S*@\\S*[.]\\S*")))
}

fun Date.parseDateWithTime(): String? {
    val format = SimpleDateFormat("HH:mm:ss", Locale("ru"))
    return format.format(this)
}

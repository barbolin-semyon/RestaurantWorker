package com.skat.restaurant.utils

/**
 * Проверка строки на валидность email-адреса
 */
fun String.emailIfValid(): Boolean {
    return (this.isEmpty() || this.matches(Regex("\\S*@\\S*[.]\\S*")))
}

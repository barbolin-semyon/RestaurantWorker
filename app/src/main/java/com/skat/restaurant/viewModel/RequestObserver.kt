package com.skat.restaurant.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Класс для отслеживания моментов загрузки данных и появления ошибок
 */
object RequestObserver {
    private val _errorMessage = MutableStateFlow(EventRequest(StatusType.NONE))
    val requestStatus: MutableStateFlow<EventRequest>
        get() = _errorMessage

    /**
     * Функция для создания события ошибки
     * @param[message] Текст ошибки, который необходимо вывести пользователю
     */
    fun showErrorMessage(message: String) {
        _errorMessage.value = EventRequest(StatusType.ERROR, message)
    }

    /**
     * Функция для создания события загрузки
     */
    fun startLoader() {
        _errorMessage.value = EventRequest(StatusType.LOADING)
    }

    /**
     * Функция для возвращения слушателя событий в начальное состояние
     */
    fun stopLoader() {
        _errorMessage.value = EventRequest(StatusType.NONE)
    }
}

/**
 * Класс, выражающий собой событие (ошибка или загрузка)
 * @param[statusType] Статус события типа [StatusType]
 * @param[message] Статус события типа [String]
 */
class EventRequest(
    val statusType: StatusType,
    val message: String = ""
)

/**
 * Класс-перечисление типов состояний события
 * NONE - ничего не происходит
 * LOADING - в данный момент идет загрузка
 * ERROR - произошла ошибка
 */
enum class StatusType() {
    NONE,
    LOADING,
    ERROR
}
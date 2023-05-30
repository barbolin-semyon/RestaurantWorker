package com.skat.restaurant.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration
import com.skat.restaurant.model.entities.History
import com.skat.restaurant.model.entities.Menu
import com.skat.restaurant.model.entities.Table
import com.skat.restaurant.model.network.RestaurantDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Thread.State
import java.util.*
import kotlin.collections.HashMap

class FirestoreViewModel : ViewModel() {
    private val db = RestaurantDataSource

    private val _tables = MutableStateFlow<List<Table>>(emptyList())
    val tables: StateFlow<List<Table>>
        get() = _tables

    private val _history = MutableStateFlow<History?>(null)
    val history: StateFlow<History?>
        get() = _history

    private val _listHistory = MutableStateFlow<List<History>>(emptyList())
    val listHistory: StateFlow<List<History>>
        get() = _listHistory

    private val _menu = MutableStateFlow<List<Menu>>(emptyList())
    val menu: StateFlow<List<Menu>>
        get() = _menu

    private val _orders = MutableStateFlow<List<HashMap<String, Any>>>(emptyList())
    val orders: StateFlow<List<HashMap<String, Any>>>
        get() = _orders

    private lateinit var snapshotListenerPlaces: ListenerRegistration

    fun enableListenerTables() {
        RequestObserver.startLoader()
        val query = db.getQueryTables()

        snapshotListenerPlaces = query
            .addSnapshotListener { value, error ->
                val body = value?.toObjects(Table::class.java)
                if (!body.isNullOrEmpty()) {
                    RequestObserver.stopLoader()
                    _tables.value = body
                } else {
                    RequestObserver.showErrorMessage(error?.message.toString())
                }
            }
    }

    fun updateTable(tableId: Int, values: HashMap<String, Any>) = viewModelScope.launch {
        db.updateTable(tableId, values)
    }

    fun changeStatusMenuItemInStopList(menuId: String, status: Boolean) = viewModelScope.launch {
        db.updateMenuItem(menuId, hashMapOf("status" to status))
    }

    fun updateHistory(historyId: String, values: HashMap<String, Any>) = viewModelScope.launch {
        db.updateHistory(historyId, values)
    }

    fun createHistory(tableId: Int, waiter: String) = viewModelScope.launch {
        val history = History(table = db.getQueryTable(tableId.toString()), waiter = waiter)
        db.createQueryHistory(history)

        updateTable(
            tableId = tableId,
            hashMapOf("status" to true, "current" to db.getQueryHistory(history.id))
        )
    }

    fun getHistory(reference: DocumentReference) = viewModelScope.launch {
        reference.addSnapshotListener { value, error ->
            _history.value = value?.toObject(History::class.java)
        }
    }

    fun getQueryMenu(id: String) = db.getQueryMenu(id)

    fun getHistory(historyId: String) = viewModelScope.launch {
        db.getHistory(historyId).addOnSuccessListener {
            _history.value = it.toObject(History::class.java)
        }
    }

    fun getHistory(keySort: String = "startTime", isReverse: Boolean = true) = viewModelScope.launch {
        db.getAllHistory(keySort, isReverse).addOnSuccessListener {
            _listHistory.value = it.toObjects(History::class.java)
        }
    }

    fun getHistoryForUser(userName: String) = viewModelScope.launch {
        db.getHistoryForUser(userName).addOnSuccessListener {
            _listHistory.value = it.toObjects(History::class.java)
        }
    }

    fun getMenu(eats: List<HashMap<String, Any>>) = viewModelScope.launch {
        val temp = mutableListOf<HashMap<String, Any>>()
        eats.forEach {
            val reference = it["reference"] as DocumentReference
            reference.get().addOnSuccessListener { doc ->
                doc.toObject(Menu::class.java)
                    ?.let { it1 ->
                        temp.add(hashMapOf("menu" to it1, "status" to it["status"]!!))
                        if (temp.size == eats.size) {
                            _orders.value = temp
                        }
                    }
            }
        }
    }

    fun getMenu() = viewModelScope.launch {
        db.getQueryMenu().addSnapshotListener { value, error ->
            value?.toObjects(Menu::class.java)?.let {
                _menu.value = it
            }
        }
    }

    fun disableListenerCollectionPlaces() = snapshotListenerPlaces.remove()
    fun updateStatusOrder(
        historyId: String,
        role: String,
        menu: List<HashMap<String, Any>>,
        menuIndex: Int
    ) = viewModelScope.launch {
        val status = if (role == "cook") "На выдаче"
        else "Выдано"

        val temp = mutableListOf<HashMap<String, Any>>()
        menu.forEachIndexed { index, hashMap ->
            temp.add(
                if (menuIndex == index) {
                    hashMapOf("reference" to hashMap["reference"]!!, "status" to status)
                } else {
                    hashMap
                }
            )
        }

        db.updateHistory(
            historyId, hashMapOf(
                "menu" to temp
            )
        )
    }
}
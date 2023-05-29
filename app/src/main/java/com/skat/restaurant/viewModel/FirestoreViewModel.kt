package com.skat.restaurant.viewModel

import androidx.lifecycle.MutableLiveData
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

    private val _menu = MutableStateFlow<List<Menu>>(emptyList())
    val menu: StateFlow<List<Menu>>
        get() = _menu

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
        db.updateTable(tableId, values).addOnSuccessListener {

        }
    }

    fun updateHistory(historyId: String, values: HashMap<String, Any>) = viewModelScope.launch {
        db.updateHistory(historyId, values)
    }

    fun createHistory(tableId: Int) = viewModelScope.launch {
        val history = History(table = db.getQueryTable(tableId.toString()))
        db.createQueryHistory(history)

        updateTable(
            tableId = tableId,
            hashMapOf("status" to true, "current" to db.getQueryHistory(history.id))
        )
    }

    fun getHistory(reference: DocumentReference) = viewModelScope.launch {
        reference.get().addOnSuccessListener {
            _history.value = it.toObject(History::class.java)
        }
    }

    fun getQueryMenu(id: String) = db.getQueryMenu(id)

    fun getHistory(historyId: String) = viewModelScope.launch {
        db.getHistory(historyId).addOnSuccessListener {
            _history.value = it.toObject(History::class.java)
        }
    }

    fun getMenu(eats: List<DocumentReference>) = viewModelScope.launch {
        val temp = mutableListOf<Menu>()
        eats.forEach {
            it.get().addOnSuccessListener {
                it.toObject(Menu::class.java)
                    ?.let { it1 ->
                        temp.add(it1)
                        if (temp.size == eats.size) {
                            _menu.value = temp
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
}
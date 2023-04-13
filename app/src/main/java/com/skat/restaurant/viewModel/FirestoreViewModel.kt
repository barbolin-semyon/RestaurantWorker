package com.skat.restaurant.viewModel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration
import com.skat.restaurant.model.entities.History
import com.skat.restaurant.model.entities.Table
import com.skat.restaurant.model.network.RestaurantDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class FirestoreViewModel : ViewModel() {
    private val db = RestaurantDataSource

    private val _tables = MutableStateFlow<List<Table>>(emptyList())
    val tables: StateFlow<List<Table>>
        get() = _tables

    private val _history = MutableStateFlow<History?>(null)
    val history: StateFlow<History?>
        get() = _history

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

    fun getHistory(reference: DocumentReference) = viewModelScope.launch {
        val query = reference.get().addOnSuccessListener {
            _history.value = it.toObject(History::class.java)
        }
    }

    fun disableListenerCollectionPlaces() = snapshotListenerPlaces.remove()
}
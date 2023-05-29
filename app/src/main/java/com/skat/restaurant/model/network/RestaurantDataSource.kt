package com.skat.restaurant.model.network

import android.annotation.SuppressLint
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.skat.restaurant.model.entities.History
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.HashMap

object RestaurantDataSource {

    @SuppressLint("StaticFieldLeak")
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val dispatcher = Dispatchers.IO

    /**
     * Получение меню с сортировкой
     */
    fun getQueryMenu(keySort: String = "", reverse: Boolean = true): Query {
        return if (reverse) {
            firestore.collection("menu")
//            .orderBy(keySort, Query.Direction.DESCENDING)
        } else {
            firestore.collection("menu")
  //          .orderBy(keySort, Query.Direction.ASCENDING)
        }
    }

    /**
     * Удаление пункта меню
     */
    suspend fun deleteMenuItem(menuItem: String) = withContext(dispatcher) {
        return@withContext firestore
            .collection("menu").document(menuItem).delete()
    }

    /**
     * Обновление меню
     */
    suspend fun updateMenuItem(menuItem: String, data: HashMap<String, Any>) =
        withContext(dispatcher) {
            return@withContext firestore
                .collection("menu").document(menuItem).update(data)
        }

    /**
     * Создание истории
     */
    fun createQueryHistory(history: History) {
        firestore.collection("history").document(history.id).set(history)
    }

    /**
     * Получение истории на определенную дату
     */
    fun getQueryHistory(date: Date): Query {
        return firestore
            .collection("history").whereEqualTo("startTime", date)
    }

    suspend fun getQueryHistoryByUser(userName: String) = withContext(Dispatchers.IO) {
        return@withContext firestore.collection("history").whereEqualTo("waiter", userName).get()
    }

    /**
     * Получение пунктов истории, где время ожидания больше конкретного времени
     */
    fun getQueryHistory(difference: Int): Query {
        return firestore.collection("history").whereGreaterThan("difference", difference)
    }

    /**
     * Получение столиков
     */
    fun getQueryTables(): Query {
        return firestore.collection("tables")
    }

    fun getQueryTable(id: String): DocumentReference {
        return firestore.collection("tables").document(id)
    }

    fun getQueryHistory(id: String): DocumentReference {
        return firestore.collection("history").document(id)
    }

    fun getQueryMenu(id: String): DocumentReference {
        return firestore.collection("menu").document(id)

    }

    /**
     * Обновление столиков
     */
    fun updateTable(tableId: Int, data: HashMap<String, Any>): Task<Void> {
        return firestore.collection("tables").document(tableId.toString()).update(data)
    }

    fun updateHistory(historyString: String, data: HashMap<String, Any>): Task<Void> {
        return firestore.collection("history").document(historyString).update(data)
    }

    fun getHistory(historyString: String): Task<DocumentSnapshot> {
        return firestore.collection("history").document(historyString).get()
    }
}


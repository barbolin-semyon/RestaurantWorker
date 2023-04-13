package com.skat.restaurant.model.network

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.skat.restaurant.model.entities.Worker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object FirebaseAuthDataSource {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val ioDispatcher = Dispatchers.IO

    fun getUser(): FirebaseUser? = auth.currentUser

    suspend fun signOut() = withContext(ioDispatcher) {
        auth.signOut()
    }

    suspend fun createUser(
        email: String,
        password: String
    ): Task<AuthResult> = withContext(ioDispatcher) {
        return@withContext auth.createUserWithEmailAndPassword(email, password)
    }

    suspend fun addUserInDb(user: Worker) = withContext(ioDispatcher){
        return@withContext db.collection("workers").document(user.id).set(user)
    }

    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Task<AuthResult> = withContext(ioDispatcher) {
        return@withContext auth.signInWithEmailAndPassword(email, password)
    }
}
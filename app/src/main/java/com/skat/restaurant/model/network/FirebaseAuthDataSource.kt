package com.skat.restaurant.model.network

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object FirebaseAuthDataSource {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
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

    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Task<AuthResult> = withContext(ioDispatcher) {
        return@withContext auth.signInWithEmailAndPassword(email, password)
    }
}
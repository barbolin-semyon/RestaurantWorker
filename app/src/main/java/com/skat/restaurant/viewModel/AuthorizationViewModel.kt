package com.skat.restaurant.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skat.restaurant.model.entities.Worker
import com.skat.restaurant.model.network.FirebaseAuthDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthorizationViewModel() : ViewModel() {

    private val db = FirebaseAuthDataSource

    private val _isAuthorization = MutableStateFlow<Boolean?>(null)
    val isAuthorization: StateFlow<Boolean?>
        get() = _isAuthorization

    fun checkAuthorization() {
        _isAuthorization.value = true
    }

    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            RequestObserver.startLoader()

            db.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    _isAuthorization.value = true
                    RequestObserver.stopLoader()
                }

                .addOnCanceledListener {
                    RequestObserver.stopLoader()
                }
                .addOnFailureListener {
                    RequestObserver.showErrorMessage("Проблема авторизации")
                }
        }
    }


    fun signOut() = viewModelScope.launch {
        _isAuthorization.value = false
        db.signOut()
    }

    fun register(worker: Worker, password: String) {
        viewModelScope.launch {
            RequestObserver.startLoader()
            db.createUser(worker.email, password)
                .addOnSuccessListener {
                    addUserInDb(worker)
                }
                .addOnCanceledListener {
                    RequestObserver.stopLoader()
                }
                .addOnFailureListener {
                    RequestObserver.showErrorMessage("Проблема Регистрации")
                }
        }
    }

    private fun addUserInDb(worker: Worker) = viewModelScope.launch {
        db.addUserInDb(worker)
            .addOnSuccessListener {
                _isAuthorization.value = true
                RequestObserver.stopLoader()
            }

            .addOnFailureListener {
                RequestObserver.showErrorMessage("Проблема Регистрации")
            }
    }
}
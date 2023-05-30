package com.skat.restaurant.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skat.restaurant.model.entities.User
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

    private val _role = MutableStateFlow("")
    val role: StateFlow<String>
        get() = _role

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?>
        get() = _user

    fun checkAuthorization() {
        _isAuthorization.value = true
    }

    fun getUser() = viewModelScope.launch {
        db.getAllUser().addOnSuccessListener {
            _user.value = it.toObject(User::class.java)
        }
    }

    fun getRoleByCode(code: String) = viewModelScope.launch {
        try {
            db.getRoleByCode(code).addOnSuccessListener {
                val temp = it.get("role")
                if (temp == null) {
                    RequestObserver.showErrorMessage("Не верный код")
                } else {
                    _role.value = temp as String
                }
            }
                .addOnFailureListener {
                    Log.i("QWE", it.message.toString())
                }

        }catch (ex: IllegalArgumentException) {
            RequestObserver.showErrorMessage("Не верный код")
        }
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

    fun register(user: User, password: String) {
        viewModelScope.launch {
            RequestObserver.startLoader()
            db.createUser(user.email, password)
                .addOnSuccessListener {
                    user.id = it.user?.uid!!
                    addUserInDb(user)
                }
                .addOnCanceledListener {
                    RequestObserver.stopLoader()
                }
                .addOnFailureListener {
                    RequestObserver.showErrorMessage("Проблема Регистрации")
                }
        }
    }

    private fun addUserInDb(user: User) = viewModelScope.launch {
        db.addUserInDb(user)
            .addOnSuccessListener {
                _isAuthorization.value = true
                RequestObserver.stopLoader()
            }

            .addOnFailureListener {
                RequestObserver.showErrorMessage("Проблема Регистрации")
            }
    }
}
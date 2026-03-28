package org.freedu.minilocationb6.viewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.freedu.minilocationb6.repo.UserRepository

class AuthViewModel(
    private val app: Application,
    private val repo: UserRepository
) : AndroidViewModel(app) {

    private val _loginResult = MutableLiveData<Pair<Boolean, String?>>()
    val loginResult: LiveData<Pair<Boolean, String?>> get() = _loginResult

    private val _registerResult = MutableLiveData<Pair<Boolean, String?>>()
    val registerResult: LiveData<Pair<Boolean, String?>> get() = _registerResult

    fun login(email: String, password: String) {
        repo.loginUser(email, password) { success, msg ->
            if (success) {
                repo.updateLocationAuto(app) { /* silent, FriendListActivity handles it */ }
            }
            _loginResult.postValue(success to msg)
        }
    }

    fun register(email: String, password: String) {
        repo.registerUser(email, password) { success, msg ->
            if (success) {
                repo.updateLocationAuto(app) { }
            }
            _registerResult.postValue(success to msg)
        }
    }
}


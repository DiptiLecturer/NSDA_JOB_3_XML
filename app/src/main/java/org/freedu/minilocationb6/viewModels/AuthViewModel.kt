package org.freedu.minilocationb6.viewModels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.freedu.minilocationb6.repo.UserRepository

class AuthViewModel(private val repo: UserRepository) : ViewModel() {

    val loginResult = MutableLiveData<Pair<Boolean, String?>>()
    val registerResult = MutableLiveData<Pair<Boolean, String?>>()
    val locationUpdated = MutableLiveData<Boolean>()

    fun login(email: String, password: String, context: Context) {
        repo.loginUser(email, password) { success, msg ->
            if (success) {
                repo.updateLocationAuto(context) {
                    locationUpdated.value = it
                }
            }
            loginResult.value = success to msg
        }
    }

    fun register(email: String, password: String, context: Context) {
        repo.registerUser(email, password) { success, msg ->
            if (success) {
                repo.updateLocationAuto(context) {
                    locationUpdated.value = it
                }
            }
            registerResult.value = success to msg
        }
    }
}


package org.freedu.minilocationb6.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.freedu.minilocationb6.Model.AppUsers
import org.freedu.minilocationb6.repo.UserRepository

class MyProfileViewModel(private val repo: UserRepository) : ViewModel() {

    private val _user = MutableLiveData<AppUsers?>()
    val user: LiveData<AppUsers?> get() = _user

    val usernameUpdateResult = MutableLiveData<Boolean>()

    fun loadUser(userId: String) {
        repo.getUserById(userId) { _user.postValue(it) }
    }

    fun updateUsername(userId: String, username: String) {
        repo.updateUsername(userId, username) { success ->
            usernameUpdateResult.postValue(success)
        }
    }

    fun shareLocation(userId: String, lat: Double, lng: Double) {
        repo.updateLocation(userId, lat, lng)
    }
}

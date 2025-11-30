package org.freedu.minilocationb6.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.freedu.minilocationb6.Model.AppUsers
import org.freedu.minilocationb6.repo.UserRepository

class FriendListViewModel(private val repo: UserRepository) : ViewModel() {

    val usersList = MutableLiveData<List<AppUsers>>()

    fun fetchUsers() {
        repo.getAllUsers { list ->
            usersList.value = list
        }
    }

    fun logout() {
        repo.logout()
    }
}

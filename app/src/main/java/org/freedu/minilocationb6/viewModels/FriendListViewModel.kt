package org.freedu.minilocationb6.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.freedu.minilocationb6.Model.AppUsers
import org.freedu.minilocationb6.repo.UserRepository

class FriendListViewModel(private val repo: UserRepository) : ViewModel() {

    private val _usersList = MutableLiveData<List<AppUsers>>()
    val usersList: LiveData<List<AppUsers>> get() = _usersList

    fun fetchUsers() {
        repo.getAllUsers { list ->
            _usersList.postValue(list)
        }
    }

    fun logout() {
        repo.logout()
    }
}

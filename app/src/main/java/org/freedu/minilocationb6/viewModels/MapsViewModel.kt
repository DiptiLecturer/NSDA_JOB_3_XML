package org.freedu.minilocationb6.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import org.freedu.minilocationb6.Model.AppUsers
import org.freedu.minilocationb6.repo.UserRepository



class MapsViewModel(private val repo: UserRepository) : ViewModel() {

    private val _singleUser = MutableLiveData<AppUsers?>()
    val singleUser: LiveData<AppUsers?> get() = _singleUser

    private val _allUsers = MutableLiveData<List<AppUsers>>()
    val allUsers: LiveData<List<AppUsers>> get() = _allUsers

    fun loadSingleUser(userId: String) {
        repo.getUserById(userId) { user ->
            _singleUser.postValue(user)
        }
    }

    fun loadAllUsers() {
        repo.getAllUsers { list ->
            _allUsers.postValue(list)
        }
    }
}


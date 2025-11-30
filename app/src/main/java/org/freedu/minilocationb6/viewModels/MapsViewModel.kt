package org.freedu.minilocationb6.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import org.freedu.minilocationb6.Model.AppUsers
import org.freedu.minilocationb6.repo.UserRepository



class MapsViewModel(private val db: FirebaseFirestore) : ViewModel() {

    private val _singleUser = MutableLiveData<AppUsers?>()
    val singleUser: LiveData<AppUsers?> get() = _singleUser

    private val _allUsers = MutableLiveData<List<AppUsers>>()
    val allUsers: LiveData<List<AppUsers>> get() = _allUsers

    fun loadSingleUser(userId: String) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { doc ->
                _singleUser.value = doc.toObject(AppUsers::class.java)
            }
    }

    fun loadAllUsers() {
        db.collection("users").get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.documents.mapNotNull { it.toObject(AppUsers::class.java) }
                _allUsers.value = list
            }
    }
}


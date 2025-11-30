package org.freedu.minilocationb6.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.freedu.minilocationb6.Model.AppUsers

class UserRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun registerUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val userId = it.user!!.uid
                val user = AppUsers(userId, email)
                db.collection("users").document(userId).set(user)
                    .addOnSuccessListener { onComplete(true, null) }
                    .addOnFailureListener { e -> onComplete(false, e.message) }
            }
            .addOnFailureListener { e -> onComplete(false, e.message) }
    }

    fun loginUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { e -> onComplete(false, e.message) }
    }

    fun getAllUsers(onComplete: (List<AppUsers>) -> Unit) {
        db.collection("users").addSnapshotListener { value, _ ->
            val list = mutableListOf<AppUsers>()
            value?.forEach { doc -> list.add(doc.toObject(AppUsers::class.java)) }
            onComplete(list)
        }
    }

    fun updateUsername(userId: String, username: String, onComplete: (Boolean) -> Unit) {
        db.collection("users").document(userId).update("username", username)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun updateLocation(userId: String, lat: Double, lng: Double) {
        db.collection("users").document(userId)
            .update(mapOf("latitude" to lat, "longitude" to lng))
    }

    fun getCurrentUserId(): String? = auth.currentUser?.uid
    fun getCurrentUserEmail(): String? = auth.currentUser?.email
    fun logout() = auth.signOut()
}

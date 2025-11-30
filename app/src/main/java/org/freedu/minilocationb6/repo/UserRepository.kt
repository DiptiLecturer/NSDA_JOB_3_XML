package org.freedu.minilocationb6.repo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.freedu.minilocationb6.Model.AppUsers

class UserRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun updateLocationAuto(context: Context, onComplete: (Boolean) -> Unit) {
        val fused = LocationServices.getFusedLocationProviderClient(context)
        val userId = auth.currentUser?.uid ?: return

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            onComplete(false)
            return
        }

        fused.lastLocation.addOnSuccessListener { loc ->
            if (loc != null) {
                val lat = loc.latitude
                val lng = loc.longitude
                updateLocation(userId, lat, lng)
                onComplete(true)
            } else {
                onComplete(false)
            }
        }
    }


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

package org.freedu.minilocationb6.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import org.freedu.minilocationb6.R
import org.freedu.minilocationb6.databinding.ActivityMapsBinding
import org.freedu.minilocationb6.viewModels.MapsViewModel
import kotlin.getValue


import org.freedu.minilocationb6.viewModels.MapsViewModelFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var map: GoogleMap

    // Use ViewModel with Factory
    private val db = FirebaseFirestore.getInstance()
    private val viewModel: MapsViewModel by viewModels { MapsViewModelFactory(db) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val showAll = intent.getBooleanExtra("showAll", false)
        val userId = intent.getStringExtra("uid")

        if (showAll) {
            viewModel.loadAllUsers()
        } else if (!userId.isNullOrEmpty()) {
            viewModel.loadSingleUser(userId)
        } else {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
        }

        // Observe single user
        viewModel.singleUser.observe(this) { user ->
            user?.let {
                if (it.latitude != null && it.longitude != null) {
                    val pos = LatLng(it.latitude, it.longitude)
                    map.addMarker(
                        MarkerOptions()
                            .position(pos)
                            .title(it.username.ifEmpty { it.email })
                    )
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 16f))
                } else {
                    Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Observe all users
        viewModel.allUsers.observe(this) { list ->
            list.forEach { user ->
                if (user.latitude != null && user.longitude != null) {
                    val pos = LatLng(user.latitude, user.longitude)
                    map.addMarker(
                        MarkerOptions()
                            .position(pos)
                            .title(user.username.ifEmpty { user.email })
                    )
                }
            }

            if (list.isNotEmpty()) {
                // Center map to Bangladesh with zoom out view
                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(23.777176, 90.399452),
                        6f
                    )
                )
            }
        }

        // No marker clicks — info window only shows title
    }
}




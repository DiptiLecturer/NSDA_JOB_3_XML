package org.freedu.minilocationb6.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.freedu.minilocationb6.Model.AppUsers
import org.freedu.minilocationb6.R
import org.freedu.minilocationb6.adapter.UserAdapter
import org.freedu.minilocationb6.databinding.ActivityFriendlistBinding
import org.freedu.minilocationb6.repo.UserRepository
import org.freedu.minilocationb6.viewModels.FriendListViewModel


class FriendListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFriendlistBinding

    // Single repo instance — no more UserRepository() scattered everywhere
    private val repo = UserRepository()

    private val viewModel by viewModels<FriendListViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FriendListViewModel(repo) as T
            }
        }
    }

    private var isMenuOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFriendlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load current user info into header
        loadCurrentUser()

        // Make My Profile header clickable
        binding.layoutMyProfile.setOnClickListener {
            val uid = repo.getCurrentUserId() ?: return@setOnClickListener
            val email = repo.getCurrentUserEmail() ?: ""
            startActivity(Intent(this, MyProfileActivity::class.java).apply {
                putExtra("uid", uid)
                putExtra("email", email)
            })
        }

        // Create adapter ONCE
        val adapter = UserAdapter { selectedUser ->
            startActivity(Intent(this, MapsActivity::class.java).apply {
                putExtra("uid", selectedUser.userId)
            })
        }

        // RecyclerView setup
        binding.userRecycler.layoutManager = LinearLayoutManager(this)
        binding.userRecycler.setHasFixedSize(true)
        binding.userRecycler.adapter = adapter

        // Observer just submits new data — no adapter recreation
        viewModel.fetchUsers()
        viewModel.usersList.observe(this) { list ->
            val currentUid = repo.getCurrentUserId()
            adapter.submitList(list.filter { it.userId != currentUid })
        }

        // FAB menu
        setupMenu()

        // Location updates
        val isFirstLogin = intent.getBooleanExtra("firstLogin", false)
        if (isFirstLogin) checkLocationPermission()
        else if (hasLocationPermission()) updateLocationAutomatically()
    }

    @SuppressLint("SetTextI18n")
    private fun loadCurrentUser() {
        val uid = repo.getCurrentUserId() ?: return
        repo.getUserById(uid) { user ->
            user?.let {
                binding.tvMyProfileName.text = it.username.ifEmpty { "No Name" }
                binding.tvMyProfileEmail.text = it.email
                binding.tvMyProfileLat.text = "Lat: ${it.latitude ?: 0.0}"
                binding.tvMyProfileLng.text = "Lng: ${it.longitude ?: 0.0}"
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkLocationPermission() {
        if (!hasLocationPermission()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                200
            )
        } else {
            updateLocationAutomatically()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 200
            && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            updateLocationAutomatically()
        }
    }

    private fun updateLocationAutomatically() {
        repo.updateLocationAuto(this) { success ->
            if (!success) Toast.makeText(this, "Automatic location update failed!", Toast.LENGTH_SHORT).show()
            else loadCurrentUser()
        }
    }

    private fun setupMenu() {
        binding.fabMain.setOnClickListener {
            if (isMenuOpen) closeMenu() else openMenu()
        }

        binding.fabProfile.setOnClickListener {
            val uid = repo.getCurrentUserId() ?: return@setOnClickListener
            val email = repo.getCurrentUserEmail() ?: ""
            startActivity(Intent(this, MyProfileActivity::class.java).apply {
                putExtra("uid", uid)
                putExtra("email", email)
            })
            closeMenu()
        }

        binding.fabShowMap.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java).apply {
                putExtra("showAll", true)
            })
            closeMenu()
        }

        binding.fabLogout.setOnClickListener {
            viewModel.logout()
            startActivity(Intent(this, AuthActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }
    }

    override fun onResume() {
        super.onResume()
        // Reset menu state so it doesn't get out of sync after returning from another screen
        isMenuOpen = false
        closeMenu()
    }

    private fun openMenu() {
        binding.fabProfile.visibility = View.VISIBLE
        binding.fabShowMap.visibility = View.VISIBLE
        binding.fabLogout.visibility = View.VISIBLE
        isMenuOpen = true
    }

    private fun closeMenu() {
        binding.fabProfile.visibility = View.GONE
        binding.fabShowMap.visibility = View.GONE
        binding.fabLogout.visibility = View.GONE
        isMenuOpen = false
    }

    private fun enableEdgeToEdge() {
        // Optional: implement if you want immersive UI
    }
}

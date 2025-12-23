package org.freedu.minilocationb6.view

import android.Manifest
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
    private val viewModel by viewModels<FriendListViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FriendListViewModel(UserRepository()) as T
            }
        }
    }

    private val userList = ArrayList<AppUsers>()
    private var isMenuOpen = false

    // Header TextViews
    private lateinit var tvMyProfileName: TextView
    private lateinit var tvMyProfileEmail: TextView
    private lateinit var tvMyProfileLat: TextView
    private lateinit var tvMyProfileLng: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendlistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        // Initialize header views
        tvMyProfileName = binding.layoutMyProfile.findViewById(R.id.tvMyProfileName)
        tvMyProfileEmail = binding.layoutMyProfile.findViewById(R.id.tvMyProfileEmail)
        tvMyProfileLat = binding.layoutMyProfile.findViewById(R.id.tvMyProfileLat)
        tvMyProfileLng = binding.layoutMyProfile.findViewById(R.id.tvMyProfileLng)

        // Load current user info
        loadCurrentUser()

        // Make My Profile header clickable
        binding.layoutMyProfile.setOnClickListener {
            val uid = UserRepository().getCurrentUserId() ?: return@setOnClickListener
            val email = UserRepository().getCurrentUserEmail() ?: ""
            startActivity(Intent(this, MyProfileActivity::class.java).apply {
                putExtra("uid", uid)
                putExtra("email", email)
            })
        }

        // RecyclerView setup
        binding.userRecycler.layoutManager = LinearLayoutManager(this)
        binding.userRecycler.setHasFixedSize(true)

        // Load friend list (exclude current user)
        viewModel.fetchUsers()
        viewModel.usersList.observe(this) { list ->
            val currentUid = UserRepository().getCurrentUserId()
            userList.clear()
            userList.addAll(list.filter { it.userId != currentUid })
            binding.userRecycler.adapter = UserAdapter(userList) { selectedUser ->
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra("uid", selectedUser.userId)
                startActivity(intent)
            }
        }

        // FAB menu
        setupMenu()

        // Optional: location updates
        val isFirstLogin = intent.getBooleanExtra("firstLogin", false)
        if (isFirstLogin) checkLocationPermission()
        else if (hasLocationPermission()) updateLocationAutomatically()
    }

    /** Load current user info including lat/lng */
    private fun loadCurrentUser() {
        val uid = UserRepository().getCurrentUserId() ?: return
        UserRepository().getUserById(uid) { user ->
            user?.let {
                tvMyProfileName.text = it.username.ifEmpty { "No Name" }
                tvMyProfileEmail.text = it.email
                tvMyProfileLat.text = "Lat: ${it.latitude ?: 0.0}"
                tvMyProfileLng.text = "Lng: ${it.longitude ?: 0.0}"
            }
        }
    }

    // Location permission & updates
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
        if (requestCode == 200 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            updateLocationAutomatically()
        }
    }

    private fun updateLocationAutomatically() {
        UserRepository().updateLocationAuto(this) { success ->
            if (!success) Toast.makeText(this, "Automatic location update failed!", Toast.LENGTH_SHORT).show()
            else loadCurrentUser() // refresh header
        }
    }

    /** Setup floating menu */
    private fun setupMenu() {
        binding.fabMain.setOnClickListener { if (isMenuOpen) closeMenu() else openMenu() }

        binding.fabProfile.setOnClickListener {
            val uid = UserRepository().getCurrentUserId() ?: return@setOnClickListener
            val email = UserRepository().getCurrentUserEmail() ?: ""
            startActivity(Intent(this, MyProfileActivity::class.java).apply {
                putExtra("uid", uid)
                putExtra("email", email)
            })
            closeMenu()
        }

        binding.fabShowMap.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java).apply { putExtra("showAll", true) })
            closeMenu()
        }

        binding.fabLogout.setOnClickListener {
            viewModel.logout()
            startActivity(Intent(this, AuthActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }
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
}

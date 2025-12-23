package org.freedu.minilocationb6.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.freedu.minilocationb6.view.AuthActivity
import org.freedu.minilocationb6.Model.AppUsers
import org.freedu.minilocationb6.adapter.UserAdapter
import org.freedu.minilocationb6.databinding.ActivityFriendlistBinding
import org.freedu.minilocationb6.repo.UserRepository
import org.freedu.minilocationb6.viewModels.FriendListViewModel
import kotlin.getValue


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendlistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        // 🔥 1. Check if this is first login
        val isFirstLogin = intent.getBooleanExtra("firstLogin", false)
        if (isFirstLogin) {
            checkLocationPermission()
        } else {
            // 🔥 If permission already granted, update automatically
            if (hasLocationPermission()) updateLocationAutomatically()
        }

        binding.userRecycler.layoutManager = LinearLayoutManager(this)

        viewModel.fetchUsers()
        setupMenu()

        viewModel.usersList.observe(this) { list ->
            userList.clear()
            userList.addAll(list)
            binding.userRecycler.adapter = UserAdapter(userList) { selectedUser ->
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra("uid", selectedUser.userId)
                startActivity(intent)
            }
        }
    }

    //  Check if permission is already granted
    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    //  Ask for permission
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

    //  If permission is granted → auto update
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 200) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                updateLocationAutomatically()
            }
        }
    }

    //  Auto update location using your repository function
    private fun updateLocationAutomatically() {
        UserRepository().updateLocationAuto(this) { success ->
            if (!success) {
                Toast.makeText(this, "Automatic location update failed!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Location auto-updated", Toast.LENGTH_SHORT).show()
            }
        }
    }

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






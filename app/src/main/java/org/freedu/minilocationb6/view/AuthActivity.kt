package org.freedu.minilocationb6.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.freedu.minilocationb6.R
import org.freedu.minilocationb6.databinding.ActivityAuthBinding
import org.freedu.minilocationb6.repo.UserRepository
import org.freedu.minilocationb6.viewModels.AuthViewModel

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    private val viewModel by viewModels<AuthViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(application, UserRepository()) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.greenPrimaryDark)

        binding.btnLogin.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val pass = binding.password.text.toString()
            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.login(email, pass)   // ← no more 'this'
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val pass = binding.password.text.toString()
            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.register(email, pass)   // ← no more 'this'
        }

        // Replace both observers with this:
        viewModel.loginResult.observe(this) { (success, msg) ->
            if (success) navigateToFriendList()
            else Toast.makeText(this, msg ?: "Login failed", Toast.LENGTH_SHORT).show()
        }

        viewModel.registerResult.observe(this) { (success, msg) ->
            if (success) navigateToFriendList()
            else Toast.makeText(this, msg ?: "Registration failed", Toast.LENGTH_SHORT).show()
        }

    }

    // Add this private function at the bottom of AuthActivity:
    private fun navigateToFriendList() {
        startActivity(Intent(this, FriendListActivity::class.java).apply {
            putExtra("firstLogin", true)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }
}



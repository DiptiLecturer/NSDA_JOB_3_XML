package org.freedu.minilocationb6.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.freedu.minilocationb6.databinding.ActivityAuthBinding
import org.freedu.minilocationb6.repo.UserRepository
import org.freedu.minilocationb6.viewModels.AuthViewModel

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val viewModel by viewModels<AuthViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(UserRepository()) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()
            viewModel.login(email, pass)
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()
            viewModel.register(email, pass)
        }

        viewModel.loginResult.observe(this) { (success, msg) ->
            if (success) startActivity(Intent(this, FriendListActivity::class.java))
            else Toast.makeText(this, msg ?: "Login failed", Toast.LENGTH_SHORT).show()
        }

        viewModel.registerResult.observe(this) { (success, msg) ->
            if (success) startActivity(Intent(this, FriendListActivity::class.java))
            else Toast.makeText(this, msg ?: "Registration failed", Toast.LENGTH_SHORT).show()
        }
    }
}
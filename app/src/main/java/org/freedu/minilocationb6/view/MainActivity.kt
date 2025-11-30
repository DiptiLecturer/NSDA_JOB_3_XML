package org.freedu.minilocationb6.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import org.freedu.minilocationb6.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_main)
        Handler(Looper.getMainLooper()).postDelayed({
            // Start FriendListActivity after 2 seconds
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish() // Close splash screen
        }, 3000) // 2000 ms = 2 seconds

    }
}
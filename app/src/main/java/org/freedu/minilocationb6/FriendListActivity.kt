package org.freedu.minilocationb6

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.freedu.minilocationb6.databinding.ActivityFriendlistBinding


class FriendListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFriendlistBinding
    private lateinit var db: FirebaseFirestore
    private val userList = ArrayList<AppUsers>()

    private var isMenuOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        binding.userRecycler.layoutManager = LinearLayoutManager(this)

        loadUsers()
        setupMenu()
    }

    private fun setupMenu() {

        binding.fabMain.setOnClickListener {
            if (isMenuOpen) closeMenu() else openMenu()
        }

        // 1️⃣ My Profile
        binding.fabProfile.setOnClickListener {
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val email = FirebaseAuth.getInstance().currentUser!!.email ?: ""

            val i = Intent(this, MyProfileActivity::class.java)
            i.putExtra("uid", uid)
            i.putExtra("email", email)
            startActivity(i)

            closeMenu()
        }

        // 2️⃣ Show all users on Map
        binding.fabShowMap.setOnClickListener {
            val i = Intent(this, MapsActivity::class.java)
            i.putExtra("showAll", true)  // Important!
            startActivity(i)
            closeMenu()
        }

        // 3️⃣ Logout
        binding.fabLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val i = Intent(this, AuthActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
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

    private fun loadUsers() {
        db.collection("users").addSnapshotListener { value, _ ->
            userList.clear()
            for (doc in value!!) userList.add(doc.toObject(AppUsers::class.java))

            binding.userRecycler.adapter = UserAdapter(userList) { selectedUser ->
                // Open MapsActivity for the clicked user
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra("uid", selectedUser.userId)
                startActivity(intent)
            }
        }
    }
}




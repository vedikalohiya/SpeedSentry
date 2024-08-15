package com.example.speedsentry

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.speedsentry.databinding.ActivityHomeBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.open,
            R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationDrawer.setNavigationItemSelectedListener(this)
        fragmentManager = supportFragmentManager
        loadAndDisplayUserData()
        openFragment(HomeFragment())
    }

    private fun loadAndDisplayUserData() {
        // Assuming that you have the user's ID (userId) available
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val firestore = FirebaseFirestore.getInstance()
            val userDocument = firestore.collection("users").document(userId)
            val headerView = binding.navigationDrawer.getHeaderView(0)
            val userNameTextView: TextView = headerView.findViewById(R.id.user_name)
            val userEmailTextView: TextView = headerView.findViewById(R.id.user_mail)

            userDocument.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userName = documentSnapshot.getString("name")
                    val userEmail = documentSnapshot.getString("email")

                    userNameTextView.text = userName
                    userEmailTextView.text = userEmail
                }
            }.addOnFailureListener { e ->
                Log.e("FirestoreError", "Error fetching user data", e)
                Toast.makeText(this, "Error fetching user data. Please try again later.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> openFragment(HomeFragment())
            R.id.profile -> openFragment(ProfileFragment())
            R.id.dashboard -> openFragment(DashboardFragment())
            R.id.help -> openFragment(HelpFragment())
            R.id.history -> openFragment(HistoryFragment())
            R.id.rewards -> openFragment(RewardsFragment())
            R.id.setting -> openFragment(SettingsFragment())
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun openFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}

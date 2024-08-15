package com.example.speedsentry

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        auth = FirebaseAuth.getInstance()
        firestore = Firebase.firestore

        val userNameTextView: TextView = view.findViewById(R.id.viewname)
        val userEmailTextView: TextView = view.findViewById(R.id.viewmail)
        val userNumberTextView: TextView = view.findViewById(R.id.viewnumber)
        val editDataButton: Button = view.findViewById(R.id.editdata)
        val forgetPassButton: Button = view.findViewById(R.id.forgetpass)

        // Load and display user data
        loadAndDisplayUserData(userNameTextView, userEmailTextView, userNumberTextView)

        editDataButton.setOnClickListener {
            // Navigate to the EditFragment
            val editFragment = EditFragment()

            val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()

            // Replace the current fragment with the EditFragment
            transaction.replace(R.id.fragment_container, editFragment)

            // Add the transaction to the back stack
            transaction.addToBackStack(null)

            // Commit the transaction
            transaction.commit()
        }

        forgetPassButton.setOnClickListener {
            val intent = Intent(requireContext(), ForgetActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun loadAndDisplayUserData(
        userNameTextView: TextView,
        userEmailTextView: TextView,
        userNumberTextView: TextView
    ) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("users").document(userId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val userName = documentSnapshot.getString("name")
                        val userEmail = documentSnapshot.getString("email")
                        val userNumber = documentSnapshot.getString("number")

                        userNameTextView.text = userName
                        userEmailTextView.text = userEmail
                        userNumberTextView.text = userNumber
                    }
                }
                .addOnFailureListener { e ->
                    // Handle error
                }
        }
    }
}

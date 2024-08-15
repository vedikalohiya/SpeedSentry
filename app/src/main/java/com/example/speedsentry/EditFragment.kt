package com.example.speedsentry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class EditFragment : Fragment() {

    private lateinit var documentReference: DocumentReference
    private lateinit var auth: FirebaseAuth
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit, container, false)

        auth = FirebaseAuth.getInstance()
        documentReference = firestore.collection("users").document(auth.currentUser?.uid ?: "")

        val editName: EditText = view.findViewById(R.id.editname)
        val editNumber: EditText = view.findViewById(R.id.editnumber)
        val saveChangesButton: Button = view.findViewById(R.id.savechanges)

        loadUserData(editName, editNumber)

        saveChangesButton.setOnClickListener {
            val newName = editName.text.toString().trim()
            val newNumber = editNumber.text.toString().trim()

            if (newName.isNotEmpty() && newNumber.isNotEmpty()) {
                // Update data in Firestore
                updateUserData(newName, newNumber)
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun loadUserData(editName: EditText, editNumber: EditText) {
        // Load data from Firestore and set it to the EditTexts
        documentReference.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val name = documentSnapshot.getString("name")
                val number = documentSnapshot.getString("number")

                editName.setText(name)
                editNumber.setText(number)
            }
        }.addOnFailureListener { e ->
            // Handle error
        }
    }

    private fun updateUserData(name: String, number: String) {
        val userData = hashMapOf(
            "name" to name,
            "number" to number
        )

        // Update data in Firestore
        documentReference.set(userData, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(
                    requireContext(),
                    "Changes saved successfully",
                    Toast.LENGTH_SHORT
                ).show()

                // Replace with the appropriate fragment
                val editFragment = ProfileFragment()
                val transaction: FragmentTransaction =
                    requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, editFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to save changes", Toast.LENGTH_SHORT).show()
            }
    }
}

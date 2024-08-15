package com.example.speedsentry

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class HomeFragment : Fragment() {

    private lateinit var sourceEditText: EditText
    private lateinit var destinationEditText: EditText
    private lateinit var startJourneyButton: Button
    private val DASHBOARD_REQUEST_CODE = 123

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize UI components
        sourceEditText = view.findViewById(R.id.source)
        destinationEditText = view.findViewById(R.id.destination)
        startJourneyButton = view.findViewById(R.id.startjourny)

        // Set click listener for the button
        startJourneyButton.setOnClickListener {
            // Get text from EditText
            val source = sourceEditText.text.toString()
            val destination = destinationEditText.text.toString()

            if (source.isEmpty() || destination.isEmpty()) {
                // Show a toast if either source or destination is empty
                Toast.makeText(requireContext(), "Enter both source and destination", Toast.LENGTH_SHORT).show()
            } else {
                val uri = Uri.parse("https://www.google.com/maps/dir/$source/$destination")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage("com.google.android.apps.maps")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

                // Start Google Maps with startActivityForResult
                startActivityForResult(intent, DASHBOARD_REQUEST_CODE)
            }
        }
        return view
    }

    // Handle the result from Google Maps
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == DASHBOARD_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                replaceWithDashboardFragment()
            }
        }
    }

    private fun replaceWithDashboardFragment() {
        val fragmentTransaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, DashboardFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        fragmentTransaction.commit()
    }
}

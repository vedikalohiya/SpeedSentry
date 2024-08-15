package com.example.speedsentry

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import java.util.Locale

private const val CHANNEL_ID = "my_notification_channel"
private const val NOTIFICATION_ID = 1

class SettingsFragment : Fragment() {

    private val REQUEST_NOTIFICATION_CODE = 101 // Unique request code for notification permission

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val languageBtn = view.findViewById<Button>(R.id.languagebtn)
        val appearanceBtn = view.findViewById<Button>(R.id.appearancebtn)
        val notificationBtn = view.findViewById<Button>(R.id.notificationbtn)
        val aboutbtn = view.findViewById<Button>(R.id.aboutbtn)

        languageBtn.setOnClickListener {
            // Implement language change functionality
        }

        appearanceBtn.setOnClickListener {
            val modes = arrayOf("Dark Mode", "Light Mode")
            AlertDialog.Builder(requireContext())
                .setTitle("Select Appearance")
                .setItems(modes) { dialog, which ->
                    // Perform action based on selected mode
                    val selectedMode = modes[which]
                    if (selectedMode == "Dark Mode") {
                        setDarkMode(true)
                    } else {
                        setDarkMode(false)
                    }
                }
                .show()
        }

        notificationBtn.setOnClickListener {
            val permissions = arrayOf(Manifest.permission.ACCESS_NOTIFICATION_POLICY)
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_NOTIFICATION_POLICY
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                createNotificationChannel()
                showNotification(
                    "Notification Permission Granted",
                    "You can now receive notifications."
                )
            } else {
                requestNotificationPermission()
            }
        }
            aboutbtn.setOnClickListener{
                   openFragment(HelpFragment())
                }

        return view
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Notification Channel" // Name of the channel
            val descriptionText = "Description of the channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setDarkMode(isDarkMode: Boolean) {
        // Implement code to set app theme to dark or light mode
        // For example, you can use AppCompatDelegate to set the app's night mode
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun requestNotificationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_NOTIFICATION_POLICY),
            REQUEST_NOTIFICATION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_NOTIFICATION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createNotificationChannel()
                showNotification(
                    "Notification Permission Granted",
                    "You can now receive notifications."
                )
            } else {
                AlertDialog.Builder(requireContext())
                    .setTitle("Permission Denied")
                    .setMessage("To receive notifications, please enable notification permission in the app settings.")
                    .setPositiveButton("OK") { dialog, which ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager = NotificationManagerCompat.from(requireContext())
        val notification = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.notification)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }
    private fun openFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }
}

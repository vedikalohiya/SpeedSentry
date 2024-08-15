package com.example.speedsentry

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction

class HelpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_help, container, false)

        val openMailBtn: ImageButton = view.findViewById(R.id.openmail)
        val reportBtn: ImageButton = view.findViewById(R.id.reportbtn)
        val feedbackBtn: ImageButton = view.findViewById(R.id.feedbackbtn)
        val mailIdTextView: TextView = view.findViewById(R.id.mailid)

        openMailBtn.setOnClickListener {
            openMail(mailIdTextView.text.toString())
        }
        reportBtn.setOnClickListener {
            openFragment(ReportFragment())
        }
        feedbackBtn.setOnClickListener {
            openFragment(FeedbackFragment())
        }
        return view
    }

    private fun openMail(mailId: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:$mailId")
        startActivity(emailIntent)
    }

    // Function to open a fragment
    private fun openFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }
}

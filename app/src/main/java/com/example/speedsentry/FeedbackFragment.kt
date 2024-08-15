package com.example.speedsentry

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class FeedbackFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var feedbackEditText: EditText
    private lateinit var submitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_feedback, container, false)

        emailEditText = view.findViewById(R.id.feedbackmail)
        feedbackEditText = view.findViewById(R.id.feedback)
        submitButton = view.findViewById(R.id.feedbacksubmit)

        submitButton.setOnClickListener {
            submitFeedback()
        }

        return view
    }

    private fun submitFeedback() {
        val email = emailEditText.text.toString()
        val feedback = feedbackEditText.text.toString()
        val emailBody = "Email: $email\nFeedback: $feedback"
        val intent = Intent(Intent.ACTION_SEND)

        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("kulkarniaditya1911@gmail.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
        intent.putExtra(Intent.EXTRA_TEXT, emailBody)

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            showToast("No email client found. Please visit our website to provide feedback.")
        }
    }

    private fun showToast(message: String) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

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

class ReportFragment : Fragment() {

    private lateinit var topicEditText: EditText
    private lateinit var detailsEditText: EditText
    private lateinit var submitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_report, container, false)
        topicEditText = view.findViewById(R.id.topic)
        detailsEditText = view.findViewById(R.id.editText)
        submitButton = view.findViewById(R.id.submitreport)

        submitButton.setOnClickListener {
            submitForm()
        }

        return view
    }

    private fun submitForm() {
        val topic = topicEditText.text.toString()
        val details = detailsEditText.text.toString()
        val emailBody = "Topic: $topic\nDetails: $details"
        val intent = Intent(Intent.ACTION_SEND)

        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("kulkarniaditya1911@gmail.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Bug Report")
        intent.putExtra(Intent.EXTRA_TEXT, emailBody)

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            showToast("No email client found. Please visit our website to report the issue.")
        }

    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}

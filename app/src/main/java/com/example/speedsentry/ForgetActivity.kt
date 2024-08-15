package com.example.speedsentry

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ForgetActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget)

        auth = FirebaseAuth.getInstance()

        val forgetPasswordEditText: EditText = findViewById(R.id.forgetpassword)
        val resetPasswordButton: Button = findViewById(R.id.sendmailbtn)

        resetPasswordButton.setOnClickListener {
            val email = forgetPasswordEditText.text.toString()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Password reset link sent", Toast.LENGTH_SHORT).show()
                    } else {
                        val exception = task.exception
                        if (exception != null) {
                            Toast.makeText(this, "Error sending reset link: ${exception.message}", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Unknown error occurred", Toast.LENGTH_SHORT).show()
                        }
                    }
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        }
    }
}

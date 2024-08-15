package com.example.speedsentry

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val emailEditText: EditText = findViewById(R.id.loginmail)
        val passwordEditText: EditText = findViewById(R.id.loginpass)
        val loginButton: Button = findViewById(R.id.loginbtn)
        val togglePassword: ImageView = findViewById(R.id.toggleLoginPassword)
        val forgetpass: Button= findViewById(R.id.forgetpass)
        val signuptxt:TextView= findViewById(R.id.signuptxt)

        togglePassword.setOnClickListener {
            val isPasswordVisible = passwordEditText.inputType == 144
            passwordEditText.inputType =
                if (isPasswordVisible) 129 else 144

            togglePassword.setImageResource(
                if (isPasswordVisible)
                    R.drawable.ic_eye
                else
                    R.drawable.ic_eye_off
            )
        }
forgetpass.setOnClickListener {
    val intent = Intent(this, ForgetActivity::class.java)
    startActivity(intent)
    finish()
}
        signuptxt.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(
                            baseContext, "Login Successful.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}

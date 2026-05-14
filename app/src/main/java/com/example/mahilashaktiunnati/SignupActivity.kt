package com.example.mahilashaktiunnati

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        val nameEt = findViewById<EditText>(R.id.nameEt)
        val emailEt = findViewById<EditText>(R.id.emailEt)
        val passwordEt = findViewById<EditText>(R.id.passwordEt)
        val confirmPasswordEt = findViewById<EditText>(R.id.confirmPasswordEt)
        val createAccountBtn = findViewById<Button>(R.id.createAccountBtn)
        val loginText = findViewById<TextView>(R.id.loginText)

        createAccountBtn.setOnClickListener {

            val name = nameEt.text.toString().trim()
            val email = emailEt.text.toString().trim()
            val password = passwordEt.text.toString().trim()
            val confirmPassword = confirmPasswordEt.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                ToastHelper.show(this, "Please fill all details")
                return@setOnClickListener
            }

            if (password.length < 6) {
                ToastHelper.show(this, "Password must be at least 6 characters")
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                ToastHelper.show(this, "Passwords do not match")
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    ToastHelper.show(this, "Account created successfully. Please login.")

                    startActivity(
                        Intent(this, LoginActivity::class.java)
                    )

                    finish()
                }
                .addOnFailureListener {
                    ToastHelper.show(
                        this,
                        it.message ?: "Account creation failed"
                    )
                }
        }

        loginText.setOnClickListener {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )

            finish()
        }
    }
}
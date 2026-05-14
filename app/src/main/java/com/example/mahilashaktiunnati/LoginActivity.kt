package com.example.mahilashaktiunnati

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val emailEt = findViewById<EditText>(R.id.emailEt)
        val passwordEt = findViewById<EditText>(R.id.passwordEt)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val createAccountBtn = findViewById<TextView>(R.id.createAccountBtn)

        // OPEN SIGNUP PAGE
        createAccountBtn.setOnClickListener {
            startActivity(
                Intent(this, SignupActivity::class.java)
            )
        }

        // LOGIN
        loginBtn.setOnClickListener {

            val email = emailEt.text.toString().trim()
            val password = passwordEt.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                ToastHelper.show(this, "Enter email & password first")
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {

                        startActivity(
                            Intent(this, MainActivity::class.java)
                        )

                        finish()

                    } else {

                        ToastHelper.show(this, "Invalid email or password")
                    }
                }
        }
    }
}
package com.example.crudapplication.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.crudapplication.databinding.ActivityLoginBinding
import com.example.crudapplication.utils.LoadingUtils
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var loadingUtils: LoadingUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)
        } catch (e: Exception) {
            Toast.makeText(this, "Error loading layout", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth = FirebaseAuth.getInstance()

        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, DashBoardActivity::class.java))
            finish()
            return
        }

        // Initialize LoadingUtils
        loadingUtils = LoadingUtils(this)

        binding.signUp.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Show loading screen
                loadingUtils.showLoading()

                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        // Hide loading screen
                        loadingUtils.dismiss()

                        if (task.isSuccessful) {
                            startActivity(Intent(this, DashBoardActivity::class.java))
                            finish()
                        } else {
                            val errorMessage = task.exception?.message ?: "Authentication failed."
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Empty fields are not allowed!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
